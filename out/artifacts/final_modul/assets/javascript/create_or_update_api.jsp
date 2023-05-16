<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<script>
    function formSubmit(data, api_url, method, callback){
        // Tạo XMLHttpRequest
        var xhr = new XMLHttpRequest();

        //Thiết lập api và phương thức
        xhr.open(method, api_url, true);
        // Thiết lập header cho request
        xhr.setRequestHeader("Content-Type", "application/json");

        // Xử lý response nếu cần
        xhr.onreadystatechange = function() {
            if(this.readyState == XMLHttpRequest.DONE) {
                console.log(this.responseText + " " + this.status)
                callback(JSON.parse(this.responseText), this.status)
            }
        };

        // Gửi đối tượng JSON
        xhr.send(JSON.stringify(data));
    }

    //Khi bấm enter, ngăn form không submit đến url được khai báo và đồng thời thực hiện gửi json
    document.addEventListener("keydown", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            setupData();
        }
    });
</script>