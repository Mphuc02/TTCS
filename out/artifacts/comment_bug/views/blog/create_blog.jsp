<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; ISO-8859-1" %>
<!DOCTYPE html>
<html>
<c:url var="api_url" value="/api-blog" />
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" href="/assets/css/text_editor/froala_editor.css">
    <link rel="stylesheet" href="/assets/css/text_editor/froala_style.css">
    <link rel="stylesheet" href="/assets/css/text_editor/code_view.css">
    <link rel="stylesheet" href="/assets/css/text_editor/image_manager.css">
    <link rel="stylesheet" href="/assets/css/text_editor/image.css">
    <link rel="stylesheet" href="/assets/css/text_editor/table.css">
    <link rel="stylesheet" href="/assets/css/text_editor/video.css">
    <link rel="stylesheet" href="/assets/css/home4.css">

    <style>
        body {
            text-align: center;
        }

        div#editor {
            width: 81%;
            margin: auto;
            text-align: left;
        }
    </style>
</head>

<body>
    <div class="web">
        <jsp:include page="/views/common/header.jsp" />
        <div id="dieuhuong">
            <div id="editor">
                <form action="" enctype="multipart/form-data">
                    <input type="hidden" id="blogId" value="${blog.blogId}">
                    <label>Tiêu đề</label>
                    <p id="error1"></p>
                    <input type="text" id="blogTitle" value="${blog.title}"> <br>

                    <label>Chọn ảnh cho tiêu đề</label>
                    <p id="error2"></p>
                    <img id="image" src="">
                    <input type="file" id="imageTitleFile" onchange="chooseFile(this)"> <br>
                        <script>
                            function chooseFile(fileInput) {
                                if (fileInput.files && fileInput.files[0]) {
                                    var reader = new FileReader();
                                    reader.onload = function (e) {
                                        var image = document.getElementById('image');
                                        image.setAttribute('src', e.target.result);
                                    };
                                    reader.readAsDataURL(fileInput.files[0]);
                                }
                            }
                        </script>

                    <p id="error3"></p>
                    <label>Chọn thể loại:</label>
                    <c:forEach var="category" items="${categories}" varStatus="loop">
                        <label>${category.name}</label>
                        <input type="checkbox" id="category-${loop.index}" value="${category.categoryId}">
                        <br>
                    </c:forEach>

                    </select>

                    <p id="error4"></p>
                    <textarea class="content-length" id='edit' style="margin-top: 30px;" placeholder="Type some text">
                    </textarea>

                    <button>
                        <c:if test="${blog.blogId == null}">Đăng bài</c:if>
                        <c:if test="${blog.blogId != null}">Cập nhật</c:if>
                    </button>
                </form>
            </div>
        </div>
    </div>
    <jsp:include page="/assets/javascript/create_or_update_api.jsp" />

    <script type="text/javascript" src="/assets/javascript/text_editor/froala_editor.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/align.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/code_beautifier.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/code_view.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/draggable.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/image.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/image_manager.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/link.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/lists.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/paragraph_format.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/paragraph_style.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/table.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/video.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/url.min.js"></script>
    <script type="text/javascript" src="/assets/javascript/text_editor/entities.min.js"></script>

    <script>

        (function () {
            const editorInstance = new FroalaEditor('#edit', {
                enter: FroalaEditor.ENTER_P,
                placeholderText: null,
                events: {
                    initialized: function () {
                        const editor = this
                        this.el.closest('form').addEventListener('submit', function (e) {
                            //console.log(editor.$oel.val())
                            content = editor.$oel.val()
                            setupData(editor.$oel.val());
                            e.preventDefault()
                        })
                    }
                }
            })
        })()

        var categories_list = []

        async function setupData(content){

            var blog_id = document.querySelector("#blogId").value
            var blog_title = document.querySelector("#blogTitle").value

            for(var i = 0 ; i < ${categories.size()}; i++){
                var idSelector = "#category-" + i
                var checked = document.querySelector(idSelector).checked
                if(checked)
                    categories_list.push(document.querySelector(idSelector).value)
            }

            if(!blog_id){
                var method = 'POST'
            }
            else{
                var method = 'PUT'
            }

            // Đọc file ảnh và mã hóa thành base64
            var fileInput = document.getElementById('imageTitleFile');

            var file = fileInput.files[0];
            if(!file) {
                document.querySelector("#error2").innerHTML = 'Ảnh tiêu đề không được để trống!'
                return
            }
            else {
                document.querySelector("#error2").innerHTML = ''
            }

            var reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = function () {
                var blog_image_title = reader.result.split(',')[1];

                var data = {
                    blogId: blog_id,
                    title: blog_title,
                    content: content,
                    categories: categories_list,
                    imageTitleData: blog_image_title
                }

                console.log(data)
                formSubmit(data, '${api_url}' , method,function (errors, status){
                    if(status != 200)
                    {
                        for(var i = 0 ; i < errors.errors.length; i++)
                        {
                            if(errors.errors[i])
                            {
                                document.querySelector("#error" + (i + 1)).innerHTML = errors.errors[i]
                            }
                            else
                            {
                                document.querySelector("#error" + (i+1)).innerHTML = ''
                            }
                        }
                    }
                    else if(status == 200)
                    {
                        console.log(errors.messages)
                        window.location.href = "blogs/" + errors.messages;
                    }
                })
            }
        }

    </script>
</body>

</html>