<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <script>
        var xhr = new XMLHttpRequest();
        xhr.open('POST', "/1234", true)
        var data = {

        }
        xhr.send(JSON.stringify(data))
        xhr.onreadystatechange = function() {
            if(this.readyState == XMLHttpRequest.DONE){
                if (this.status == 200) {
                    console.log("ok")
                    alert(xhr.responseText)
                }
                else
                    console.log(xhr.responseText)
            }
        };
    </script>
</body>
</html>
