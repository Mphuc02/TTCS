<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="api_url" value="/api-blog"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="/assets/css/admin/admin3.css">
    <link rel="stylesheet" href="/assets/css/home4.css">
    <title>Admin</title>
</head>
<body>
    <div id="Admin">
        <div class="navbar-main">
            <jsp:include page="/views/common/header.jsp" />
        </div>
        <div id="main">
            <!-- MAIN -->
            <div class="container">
                <div class="navbar">
                    <div class="icon--link">
                        <i class="icon fa-solid fa-book-open"></i>
                    </div>
                </div>
            </div>
            <!-- Content -->
            <section class="content">
                <div class="row mt-5">
                    <div class="col">
                        <div class="card">
                            <div class="card-header">
                                <div class="float-left">
                                    <form action="#">
                                        <div class="tim-kiem">
                                            <input type="text" placeholder="Tìm Kiếm" class="search">
                                            <button class="btn btn-search">
                                                <a href="#" class="title">Tìm Kiếm</a>
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <div class="card-body">
                                <table class="table" style="border: solid 1px #000;">
                                    <thead class="thead-dark">
                                    <tr>
                                        <th>STT</th>
                                        <th>Tên</th>
                                        <th>Người đăng</th>
                                        <th>Thời gian đăng</th>
                                        <th>Lượt thích</th>
                                        <th>Trạng thái</th>
                                        <th>Hành động</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="blog" items="${blogList}" varStatus="loop">
                                            <tr>
                                                <a href="/blogs/${blog.blogId}">
                                                    <td>${loop.index + 1}</td>
                                                    <td>${blog.title}</td>
                                                    <td>${blog.user.userId}</td>
                                                    <td>${blog.createdAt}</td>
                                                    <td>0</td>
                                                    <td>
                                                        <c:if test="${blog.status == 0}"><p id="blog-status">Đã bị ẩn</p></c:if>
                                                        <c:if test="${blog.status == 1}"><p id="blog-status">Đã được đuyệt</p></c:if>
                                                        <c:if test="${blog.status == 2}"><p id="blog-status">Đang chờ xét duyệt</p></c:if>
                                                    </td>
                                                    <td>
                                                        <select onchange="setupData(${blog.blogId}, this.value)">
                                                            <c:if test="${blog.status==0}">
                                                                <option value="0" >Ẩn truyện này</option>
                                                                <option value="1" >Công khai truyện này</option>
                                                            </c:if>
                                                            <c:if test="${blog.status==1}" >
                                                                <option value="0" >Ẩn truyện này</option>
                                                                <option value="1" selected>Công khai truyện này</option>
                                                            </c:if>
                                                            <c:if test="${blog.status==2}" >
                                                                <option value="-1" ></option>
                                                                <option value="0" >Ẩn truyện này</option>
                                                                <option value="1">Công khai truyện này</option>
                                                            </c:if>
                                                        </select>
                                                    </td>
                                                </a>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="card-footer">
                                <nav class="Page navigation">
                                    <ul class="pagination jc-center" id="pagination"></ul>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
    <jsp:include page="/assets/javascript/pagination.jsp" />
    <jsp:include page="/assets/javascript/create_or_update_api.jsp"/>

    <script>
        initPagination('${blogs_page}');

        function setupData(blogId, status){
            if(status == -1)
                return
            var data = {
                blogId: blogId,
                status: status
            }

            console.log(data)

            formSubmit(data, '${api_url}', 'DELETE', function (errors, status)
            {
                if(status == 200) {
                    var blogStatus = document.querySelector("#blog-status")
                    alert(errors.messages)
                    window.location.reload()
                }
                else{
                    alert(errors.errors[0])
                }
            })
        }
    </script>
</body>
</html>