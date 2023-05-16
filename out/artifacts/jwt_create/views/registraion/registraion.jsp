<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="api_url" value="/api-create-user" />
<!DOCTYPE html>
<html lang="en">
<head>
  <link rel="stylesheet" href="../../assets/css/registraion/registraion5.css">
  <title>Blog Truyện - Đăng kí</title>
</head>
<body>
    <div class="main">
        <div class="header">
            <div class="header__navbar">
                <a href="#" class="header__navbar--main">
                    BlogTruyen.vn
                </a>
            </div>
        </div>
        <section>
            <div class="form-box">
                <div class="form-value">
                    <h2>Đăng kí</h2>
                    <div class="inputbox">
                        <input type="text" name="userName" id="user-name" required>
                        <label>Tên đăng nhập</label>
                    </div>
                    <span class="bug" id="bug1">${bug1}</span>
                    <div class="inputbox">
                        <input type="password" name="passWord" id="pass-word" required>
                        <label>Mật khẩu</label>
                    </div>
                    <span class="bug" id="bug2">${bug2}</span>
                    <div class="inputbox">
                        <input type="password" name="passWord-2" id="re-pass-word" required>
                        <label>Nhập lại mật khẩu</label>
                    </div>
                    <span class="bug" id="bug3">${bug3}</span>
                    <div class="inputbox">
                        <input type="mail" name="email" id="email" required>
                        <label>Email</label>
                    </div>
                    <span class="bug" id="bug4">${bug4}</span>
                    <button class="btn" type="button" onclick="setupData()">Đăng ký</button>
                    <div class="register">
                        <p>Bạn đã có tài khoản rồi ? <a href="../../login">Đăng nhập</a></p>
                    </div>
                </div>
            </div>
        </section>
    </div>
    <jsp:include page="/assets/javascript/create_or_update_api.jsp" />
    <script>
        async function setupData() {
            var userName = document.querySelector("#user-name").value;
            var passWord = document.querySelector("#pass-word").value;
            var rePassWord = document.querySelector("#re-pass-word").value;
            var email = document.querySelector("#email").value;
            var data = {
                userName: userName,
                passWord: passWord,
                re_password: rePassWord,
                email: email
            }
            formSubmit(data, '${api_url}', 'POST', function(errors, status) {
                if(status != 200) {
                    for(var  i = 0 ; i < errors.errors.length; i++) {
                        if(errors.errors[i])
                            document.querySelector("#bug" + (i + 1)).innerHTML = errors.errors[i]
                        else
                            document.querySelector("#bug" + (i + 1)).innerHTML = ''
                    }
                } else if(status == 200){
                    window.location.href = "/login?action=sign-up-success";
                }
            });
        }
    </script>
</body>
</html>