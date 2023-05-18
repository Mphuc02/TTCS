<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="api_url" value="/api-admin-category"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="../../../assets/css/admin/admin_create1.css">
    <title>Admin</title>
</head>
<body>
    <div id="Admin">
        <div id="main">
            <!-- MAIN -->
            <div class="container">
                <div class="navbar">
                    <div class="icon--link">
                        <i class="icon fa-solid fa-book-open"></i>
                    </div>
                    <div class="navbar-main">
                        <ul class="navbar__list">
                            <li class="navbar__item">
                                <a href="#" class="navbar__item--link">Quản lý thể loại</a>
                            </li>
                            <li class="navbar__item">
                                <a href="/admin/blogs" class="navbar__item--link">Quản lý truyện</a>
                            </li>
                            <li class="navbar__item">
                                <a href="/admin/users" class="navbar__item--link">Quản lý người dùng</a>
                            </li>
                        </ul>
                        <button class="btn-exist">
                            <a href="/login?action=logout" class="btn-exist--link">Đăng xuất</a>
                        </button>
                    </div>
                </div>
            </div>
            <!-- Content -->
            <section class="content">
                <div class="row mt-5">
                    <div class="col-6 mx-auto">
                        <form action="#" method="post">
                            <input type="hidden" name="_csrf" value="5bbb96ac-26ce-4f51-9d7c-3b71a2dae617">
                            <div class="card">
                                <div class="card-header">
                                    <h2>Thêm thể loại</h2>
                                </div>
                                <div class="card-body">
                                    <form class="form-group mt-4" id="formSubmit" action="/admin/categories/edit/-1" method="post">
                                        <input type="hidden" id="categoryId" name="categoryId" value = ${category.categoryId}>
                                        <input type="hidden" id="userId" value="${category.userId}">
                                        <input type="hidden" id="createdAt" value="${category.createdAt}">

                                        <label>Thể loại</label>
                                        <input type="text" class="form-control" name="name" id="name" placeholder="Tên thể loại" value="${category.name}">

                                        <button type="button" onclick="formSubmit()" class="btn btn-success mr-2">
                                            <i class="fa-solid fa-floppy-disk mr-1"></i>
                                            <a href="#" class="title">Lưu</a>
                                        </button>

                                    </form>
                                </div>
                                <div class="card-footer">
                                    <a href="/admin/categories" class="btn btn-primary">
                                        <i class="fa-solid fa-list mr-1"></i>
                                        Danh sách thể loại
                                    </a>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <script>
        function formSubmit(){
            var categoryId = document.querySelector("#categoryId").value;
            var nameStr = document.querySelector("#name").value;

            // Tạo XMLHttpRequest
            var xhr = new XMLHttpRequest();

            if(!categoryId)
            {
                var data = {
                    categoryId: categoryId,
                    name: nameStr
                }
                // Thiết lập phương thức POST và URL của API để gửi JSON
                xhr.open("POST", '${api_url}', true);
            }
            else
            {
                var data = {
                    categoryId: categoryId,
                    name: nameStr,
                    userId: document.querySelector("#userId").value,
                    createdAt: document.querySelector("#createdAt").value
                }
                // Thiết lập phương thức PUT và URL của API để gửi JSON
                xhr.open("PUT", '${api_url}', true);
            }
            // Thiết lập header cho request
            xhr.setRequestHeader("Content-Type", "application/json");
            // Gửi đối tượng JSON
            xhr.send(JSON.stringify(data));

            // Xử lý response nếu cần
            xhr.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    console.log(this.responseText);
                    if(this.responseText)
                    {
                        if(!categoryId)
                            alert("Thêm thể loại thành công!")
                        else
                            alert("Cập nhật thành công!")
                    }
                }
            };
        }

        //Khi bấm enter, ngăn form không submit đến url được khai báo và đồng thời thực hiện gửi json
        document.addEventListener("keydown", function(event) {
            if (event.key === "Enter") {
                event.preventDefault();
                formSubmit();
            }
        });
    </script>
</body>
</html>