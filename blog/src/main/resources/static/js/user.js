let index = {
    //let _this = this;
    init: function () {
        // 만약 functon() {}을 사용 시, 내부 this값은 window를 가리키게 됨. 사용하고자한다면, 위에 내부에서 this를 바인딩해줘야함.
        $("#btn-save").on("click", () => {      // this를 바인딩하기 위해 화살표 함수 사용하여 해당 내부의 값을 가리키도록 함!
            this.save();
        } );
    },
    save: function () {
        // alert('user의 save함수 호출됨');
        let data = {
            username: $("#username").val(),
            email: $("#email").val(),
            password: $("#password").val()
        };
        // console.log(data);
        // ajax 호출 시 default가 비동기 호출
        // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert요청
        // ajax가 통신 성공 후 서버가 json을 리턴해주면  자동으로 자바 오브젝트로 변환해준다.
        $.ajax({
            // 회원가입 수행 요청
            type:"POST",
            url: "/blog/api/user",
            data: JSON.stringify(data),      // json화하여 body로 전송
            contentType: "application/json; charset=utf-8",     // body 데이터의 타입
            // dataType: "json"    // 서버로부터의 응답데이터 타입 (기본적으로 byte로 와서 String으로 변환) => javascript 오브젝트로 변환해서 응답옴
        }).done(function(res) { // javascript 오브젝트로 res에 들어옴
            // UserApiController에서 new ResponseDto<Integer>(200, 1); 를 생성해서 return하니
            // {status: 200, data: 1} 로 반환됨.
            // status는 통신 결과, data는 데이터
            alert("회원가입이 완료되었습니다.");
            console.log(res);
            // location.href="/blog";
        }).fail(function (err) {
            alert(JSON.stringify(err));
        });

    }
}

index.init();