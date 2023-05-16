package com.example.btl_web.service.impl;

import com.example.btl_web.dao.BlogDao;
import com.example.btl_web.dao.UserDao;
import com.example.btl_web.dao.impl.BlogDaoImpl;
import com.example.btl_web.dao.impl.UserDaoImpl;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.CategoryDto;
import com.example.btl_web.dto.CommentDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.model.Blog;
import com.example.btl_web.model.User;
import com.example.btl_web.paging.PageRequest;
import com.example.btl_web.paging.Pageable;
import com.example.btl_web.service.BlogService;
import com.example.btl_web.service.CategoryService;
import com.example.btl_web.service.UserBlogService;
import com.example.btl_web.service.UserService;
import com.example.btl_web.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
public class BlogServiceImpl implements BlogService {
    private BlogDao blogDao = BlogDaoImpl.getInstance();
    private UserDao userDao = UserDaoImpl.getInstance();
    private UserBlogService userBlogService = UserBlogServiceImpl.getInstance();
    private static BlogServiceImpl blogService;
    private static CategoryService categoryService = CategoryServiceImpl.getInstance();
    public static BlogServiceImpl getInstance()
    {
        if(blogService == null)
            blogService = new BlogServiceImpl();
        return blogService;
    }

    @Override
    public List<BlogDto> getAllBlogs(Pageable pageable, BlogDto dto) {
        StringBuilder sql = new StringBuilder("SELECT * FROM BLOGS b");

        sql.append(addAndClause(pageable, dto));
        List<Blog> blogs = blogDao.findAll(sql.toString());
        List<BlogDto> dtos = new ArrayList<>();

        for(Blog blog: blogs)
        {
            dtos.add(ConvertUtils.convertEntityToDto(blog, BlogDto.class));
        }

        return dtos;
    }
    @Override
    public BlogDto getOneById(Long blogId) {
        BlogDto blog = new BlogDto();
        blog.setBlogId(blogId);

        List<BlogDto> blogDtos = getAllBlogs(null, blog);
        if(blogDtos.isEmpty())
            return null;
        blog = blogDtos.get(0);
        blog.setCategoriesList(categoryService.findAllCategoryOfBlog(blogId, 1));
        blog.setLikedUsers(peopleLikedBlog(blogId));

        CommentDto commentDto = new CommentDto();
        commentDto.setBlogComment(blogId);
        Pageable pageable = new PageRequest(new HashMap<>(), 10L);
        List<CommentDto> commentsOfBlog = userBlogService.findAll(pageable, commentDto);
        blog.setComments(commentsOfBlog);

        return blog;
    }

    @Override
    public long countBlogs(BlogDto blogDto) {
        StringBuilder sql = new StringBuilder("SELECT count(b.blog_id) FROM BLOGS b");
        sql.append(addAndClause(null, blogDto));
        return blogDao.countBlogs(sql.toString());
    }

    @Override
    public Long save(BlogDto blog) {
        Date timeStamp = new Date();
        String sql = "INSERT INTO BLOGS (content, created_at, title, user_id, status) values (?, ?, ?, ?, 2)";

        Long saveBlog = blogDao.save(sql, blog.getContent(), timeStamp.getTime(), blog.getTitle(), blog.getUser().getUserId());
        if(saveBlog == null)
            return null;
        //Lưu các thể loại của truyện này
        Long saveCategories = categoryService.saveCategoriesOfBlog(saveBlog, blog.getCategories());
        if(saveCategories == null)
            return null;

        //Lưu hoạt động gần đây nhất của User
        UserService userService = new UserServiceimpl();
        userService.updateLastAction(blog.getUser());
        return saveBlog;
    }

    @Override
    public Long update(BlogDto blog) {
        StringBuilder sql = new StringBuilder("UPDATE BLOGS SET blog_id = " + blog.getBlogId());
        sql.append(addUpdateClause(blog));
        return blogDao.save(sql.toString());
    }

    @Override
    public boolean validCreateBlog(String[] errors, BlogDto blog) {
        if(!validUpdateBlog(errors, blog))
            return false;

        boolean result = true;
        if(blog.getTitle().isEmpty())
        {
            result = false;
            errors[0] = "Tiêu đề không được để trống";
        }
        if(blog.getImageTitleData() == null)
        {
            result = false;
            errors[1] = "Ảnh tiêu đề không được để trống";
        }
        if(blog.getCategories().isEmpty())
        {
            result = false;
            errors[2] = "Phải chọn ít nhất 1 thể loại";
        }

        if(blog.getContent().isEmpty())
        {
            result = false;
            errors[3] = "Nội dung truyện không được để trống";
        }
        return result;
    }

    @Override
    public boolean validUpdateBlog(String[] errors, BlogDto blog) {
        UserService userService = new UserServiceimpl();
        Long validTime = userService.checkLastAction(blog.getUser().getUserId());
        if(validTime != null)
        {
            errors[0] = "Bạn thao tác quá nhanh, vui lòng thử lại sau " + validTime;
            return false;
        }

        BlogDto dto = new BlogDto();
        dto.setBlogId(blog.getBlogId());
        List<BlogDto> checkBlogExisted = getAllBlogs(null, dto);
        if(checkBlogExisted == null || checkBlogExisted.isEmpty())
            return false;

        return true;
    }

    private List<UserDto> peopleLikedBlog(Long blogId) {
        String sql = "Select u.user_id, u.username, u.full_name from Users u, liked l where l.user_id = u.user_id and l.blog_id = " + blogId;

        List<User> users = userDao.findAllUserInclude(sql);
        List<UserDto> result = new ArrayList<>();
        if(!users.isEmpty())
        {
            for(User user: users)
            {
                result.add(ConvertUtils.convertEntityToDto(user, UserDto.class));
            }
        }
        return result;
    }

    @Override
    public boolean checkUserLikedBlog(BlogDto blog, Long userId) {
        return false;
    }

    private StringBuilder addAndClause(Pageable pageable ,BlogDto dto)
    {
        StringBuilder sb = new StringBuilder();

        if(dto != null)
        {
            boolean firstWhere = true;
            List<CategoryDto> categories = dto.getCategories();
            if(categories != null)
            {
                firstWhere = false;
                sb.append(", BLOGS_CATEGORIES b_c WHERE ( 1 = 1) AND b_c.blog_id = b.blog_id");
                for(CategoryDto category: categories)
                {
                    sb.append(" AND b_c.category_id = " + category.getCategoryId());
                }
            }

            if(firstWhere)
                sb.append(" WHERE (1 = 1)");

            Long blogId = dto.getBlogId();
            String title = dto.getTitle();
            String content = dto.getContent();
            String imageTitle = dto.getImageTitle();
            String createAt = dto.getCreatedAt();
            Integer status = dto.getStatus();

            if (blogId != null)
                sb.append(" AND blog_id = " + blogId);
            if (title != null)
                sb.append(" AND title like '%" + title + "%'");
            if (content != null)
                sb.append(" AND content like '%" + content + "%'");
            if (imageTitle != null)
                sb.append(" AND image_title = " + imageTitle);
            if (createAt != null)
                sb.append(" AND created_at = " + createAt);
            if (status != null)
                sb.append(" AND status = " + status);
        }

        if(pageable != null)
            sb.append(pageable.addPagingation());

        return sb;
    }
    private StringBuilder addUpdateClause(BlogDto blog)
    {
        Long blogId = blog.getBlogId();
        String title = blog.getTitle();
        String content = blog.getContent();
        String imageTitle = blog.getImageTitle();
        String createAtStr  = blog.getCreatedAt();
        Long createAt = null;
        if(createAtStr != null)
            createAt = ConvertUtils.convertStringDateToLong(createAtStr);
        Integer status = blog.getStatus();

        StringBuilder sb = new StringBuilder();
        if(title != null)
            sb.append(", title = '" + title + "'");
        if(content != null)
            sb.append(", content '" + content + "'");
        if(imageTitle != null)
            sb.append(", content '" + content + "'");
        if(createAt != null)
            sb.append(", created_at = " + createAt);
        if(status != null)
            sb.append(", status = " + status );
        sb.append(" WHERE blog_id = " + blogId);
        return sb;
    }
}
