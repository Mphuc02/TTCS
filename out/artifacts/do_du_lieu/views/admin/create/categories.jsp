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
    <link rel="stylesheet" href="/assets/css/home4.css">
    <title>Admin</title>
</head>
<body>
    <div id="Admin">
        <div class="navbar-main">
            <jsp:include page="/views/common/header.jsp" />
        </div>
        <div id="main">
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
                                    <form class="form-group mt-4" id="formSubmit">
                                        <input type="hidden" id="categoryId" name="categoryId" value = ${category.categoryId}>
                                        <input type="hidden" id="userId" value="${category.userId}">
                                        <input type="hidden" id="createdAt" value="${category.createdAt}">

                                        <label>Thể loại</label>
                                        <p id="name-error"></p>
                                        <input type="text" class="form-control" name="name" id="name" placeholder="Tên thể loại" value="${category.name}">

                                        <button type="button" onclick="setupData()" class="btn btn-success mr-2">
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

    <jsp:include page="/assets/javascript/create_or_update_api.jsp" />
    <script>
        function setupData()
        {
            var categoryId = document.querySelector("#categoryId").value;
            var nameStr = document.querySelector("#name").value;

            if(!categoryId)
            {
                var data = {
                    name: nameStr
                }
                // Thiết lập phương thức POST và URL của API để gửi JSON
                var method = "POST"
            }
            else
            {
                var data = {
                    categoryId: categoryId,
                    name: nameStr,
                    userId: document.querySelector("#userId").value,
                    createdAt: document.querySelector("#createdAt").value
                }
                var method = "PUT"
            }

            console.log(data)

            formSubmit(data, '${api_url}', method, function (errors, status){
                if(status == 200)
                {
                    alert(errors.messages)
                    window.location.reload()
                }
                else{
                    initProblems(errors)
                }
            })
        }

        function initProblems(errors){
            var nameError = document.querySelector("#name-error")
            nameError.innerHTML = errors.errors[0]
        }
    </script>
</body>
</html>