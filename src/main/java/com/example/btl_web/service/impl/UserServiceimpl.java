package com.example.btl_web.service.impl;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.dao.UserDao;
import com.example.btl_web.dao.impl.UserDaoImpl;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.model.User;
import com.example.btl_web.paging.Pageable;
import com.example.btl_web.service.HashPasswordService;
import com.example.btl_web.service.UserService;
import com.example.btl_web.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class UserServiceimpl implements UserService {
    private UserDao userDao = UserDaoImpl.getInstance();
    private HashPasswordService hashPasswordService = ServiceConfiguration.getHashPasswordService();
    @Override
    public List<UserDto> findAll(Pageable pageable, UserDto dto) {
        StringBuilder sql = new StringBuilder("SELECT * FROM USERS WHERE ( 1 = 1)");
        sql.append(addAndClause(pageable, dto));

        List<User> users = userDao.findAll(sql.toString());
        List<UserDto> dtos = new ArrayList<>();

        for(User user: users)
        {
            dtos.add(ConvertUtils.convertEntityToDto(user, UserDto.class));
        }
        return dtos;
    }

    @Override
    public List<UserDto> findAllInclude(Pageable pageable, UserDto dto) {
        StringBuilder sql = new StringBuilder("SELECT * FROM USERS WHERE ( 1 = 1)");
        sql.append(addAndClause(pageable, dto));

        List<User> users = userDao.findAllUserInclude(sql.toString());
        List<UserDto> dtos = new ArrayList<>();

        for(User user: users)
        {
            dtos.add(ConvertUtils.convertEntityToDto(user, UserDto.class));
        }
        return dtos;
    }

    @Override
    public UserDto findOneById(Long userId) {
        UserDto findUser = new UserDto();
        findUser.setUserId(userId);
        List<UserDto> result = findAll(null, findUser);
        if(result == null || result.isEmpty())
            return null;
        return result.get(0);
    }


    @Override
    public UserDto login(String userName, String passWord) {
        UserDto userDto = new UserDto();
        userDto.setUserName(userName);
        String encryptPassword = hashPasswordService.encryptPassword(passWord); //Mã hoá password này rồi thực hiện tìm kiếm trong database
        userDto.setPassWord(encryptPassword);

        StringBuilder sql = new StringBuilder("SELECT * FROM USERS WHERE (1 = 1)");
        sql.append(addAndClause(null, userDto));

        List<User> users = userDao.getUserByCondition(sql.toString());

        return users.isEmpty() ? null : ConvertUtils.convertEntityToDto(users.get(0), UserDto.class);
    }
    @Override
    public Long saveUser(UserDto userDto) {
        String encryptPassword = hashPasswordService.encryptPassword(userDto.getPassWord());
        userDto.setPassWord(encryptPassword); //Mã hoá password
        Date timeStamp = new Date();
        String sql = "INSERT INTO USERS (email, password, created_at, role, username, last_action, status) VALUES (?, ?, ?, ?, ?, ?, 1)";
        return userDao.saveUser(sql,userDto.getEmail(), userDto.getPassWord(), timeStamp.getTime(), "USER", userDto.getUserName(), timeStamp.getTime());
    }

    @Override
    public Long updateUser(UserDto dto) {
        StringBuilder sql = new StringBuilder("UPDATE USERS SET user_id = " + dto.getUserId()) ;
        sql.append(addUpdateClause(dto));
        return userDao.saveUser(sql.toString());
    }

    @Override
    public boolean validateSignUp(UserDto user, String[] errors) {
        boolean check = true;

        if(user.getUserName() == null)
        {
            errors[0] = "Tên đăng nhập không được bỏ trống!";
            check = false;
        }
        UserDto checkUserNameExisted = new UserDto();
        checkUserNameExisted.setUserName(user.getUserName());
        if(!findAll(null, checkUserNameExisted).isEmpty())
        {
            errors[0] = "Tên đăng nhập này đã tồn tại!";
            check = false;
        }

        if(user.getPassWord() == null)
        {
            check = false;
            errors[1] = "Mật khẩu không được để trống!";
        }
        String passWord = user.getPassWord();
        if(passWord.length() < 6)
        {
            check = false;
            errors[1] = "Mật khẩu phải có độ dài ít nhất 6 ký tự!";
        }

        if(user.getRe_password() == null)
        {
            check = false;
            errors[2] = "Mật khẩu nhập lại không được để trống!";
        }
        if(!user.getPassWord().equals(user.getRe_password()))
        {
            check = false;
            errors[2] = "Mật khẩu nhập lại không khớp";
        }

        if(!checkEmailValid(user.getEmail()))
        {
            check = false;
            errors[3] = "Email không đúng định dạng";
        }
        UserDto checkEmailExisted = new UserDto();
        checkEmailExisted.setEmail(user.getEmail());
        if(!findAll(null, checkEmailExisted).isEmpty())
        {
            check = false;
            errors[3] = "Email này đã được đăng ký";
        }

        return check;
    }

    @Override
    public boolean validUpdate(UserDto user, String[] errors) {
        String timeValid = checkLastAction(user.getUserId());
        if(timeValid != null)
        {
            errors[0] = timeValid;
            return false;
        }

        UserDto dto = new UserDto();
        List<UserDto> existedUser = findAll(null, dto);
        if(existedUser == null || existedUser.isEmpty())
        {
            errors[0] = "Người dùng không tồn tại";
            return false;
        }
        return true;
    }

    @Override
    public boolean updateLastAction(UserDto user) {
        Date timeLastACtion = new Date();
        user.setLastAction(timeLastACtion.getTime());
        return updateUser(user) != null;
    }

    @Override
    public String checkLastAction(Long userId) {
        UserDto validUser = findOneById(userId);
        Long timenow = (new Date()).getTime();
        Long lastAction = validUser.getLastAction();
        Long validTime = (timenow - lastAction) / 1000;

        if(validTime < 30)
            return "Bạn thao tác quá nhanh, vui lòng thử lại sau " + (30 - validTime);
        else
            updateLastAction(validUser);
        return null;
    }

    private boolean checkUserNameExisted(String userName)
    {
        String sql = "SELECT * FROM USERS WHERE username = ?";
        List<User> users = userDao.getUserByCondition(sql.toString(), userName);

        User user = users.isEmpty() ? null: users.get(0);

        return user != null;
    }
    @Override
    public long countUsers(UserDto countDto) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(user_id) FROM USERS where (1 = 1)");
        sql.append(addAndClause(null, countDto));

        return userDao.countItems(sql.toString());
    }
    private StringBuilder addAndClause(Pageable pageable,UserDto userDto)
    {
        StringBuilder sb = new StringBuilder();
        if(userDto != null)
        {
            Long userId = userDto.getUserId();
            Integer status = userDto.getStatus();
            String userName = userDto.getUserName();
            String passWord = userDto.getPassWord();
            String email = userDto.getEmail();
            String role = userDto.getRole();
            String address = userDto.getAddress();
            String phone = userDto.getPhone();
            String fullName = userDto.getFullName();
            String registeredAt = userDto.getRegisteredAt();

            if(userId != null)
                sb.append(" AND user_id = " + userId );
            if(status != null)
                sb.append(" AND status = " + status);
            if(userName != null)
                sb.append(" AND username = '" + userName + "'");
            if(passWord != null)
                sb.append(" AND password = '" + passWord + "'");
            if(email != null)
                sb.append(" AND email = '" + email + "'");
            if(role != null)
                sb.append(" AND role = '" + role + "'");
            if(address != null)
                sb.append(" AND address = '" + address + "'");
            if(phone != null)
                sb.append(" AND phone = '" + phone +  "'");
            if(fullName != null)
                sb.append(" AND lower(full_name) like lower('%" + fullName + "%')");
            if(registeredAt != null)
                sb.append(" AND registered_at = " + registeredAt);
        }

        if(pageable != null)
            sb.append(pageable.addPagingation());

        return sb;
    }
    private StringBuilder addUpdateClause(UserDto dto)
    {
        StringBuilder sb = new StringBuilder();
        String userName = dto.getUserName();
        String passWord = dto.getPassWord();
        String email = dto.getEmail();
        String role = dto.getRole();
        String address = dto.getAddress();
        String phone = dto.getPhone();
        String fullName = dto.getFullName();
        Long timeStamp = null;
        Long lastAcion = dto.getLastAction();

        if(dto.getRegisteredAt() != null)
            timeStamp = ConvertUtils.convertStringDateToLong(dto.getRegisteredAt());
        Integer status = dto.getStatus();

        if(userName != null)
            sb.append(", username = '" + userName + "'");
        if(passWord != null)
            sb.append(", password = '" + passWord + "'");
        if(email != null)
            sb.append(", email = '" + email + "'");
        if(role != null)
            sb.append(", role = '" + role + "'");
        if(address != null)
            sb.append(", address = '" + address + "'");
        if(phone != null)
            sb.append(", phone = '" + phone + "'");
        if(fullName != null)
            sb.append(", full_name = '" + fullName + "'");
        if(timeStamp != null)
            sb.append(", created_at = " + timeStamp);
        if(status != null)
            sb.append(", status = " + status);
        if(lastAcion != null)
            sb.append(", last_action = " + lastAcion);

        sb.append(" WHERE user_id = " + dto.getUserId());
        return sb;
    }
    private boolean checkEmailValid(String email)
    {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }
}
