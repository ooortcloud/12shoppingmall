<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<!-- <link rel="stylesheet" href="/css/admin.css" type="text/css">  -->

	<!-- 참조 : http://getbootstrap.com/css/   참조 -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	
	<!--  ///////////////////////// Bootstrap, jQuery CDN ////////////////////////// -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" >
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" >
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" ></script>
	
	
	<!-- Bootstrap Dropdown Hover CSS -->
   <link href="/css/animate.min.css" rel="stylesheet">
   <link href="/css/bootstrap-dropdownhover.min.css" rel="stylesheet">
    <!-- Bootstrap Dropdown Hover JS -->
   <script src="/javascript/bootstrap-dropdownhover.min.js"></script>

  
	<!--  CSS 추가 : 툴바에 화면 가리는 현상 해결 :  주석처리 전, 후 확인-->
	<style>
        body {
            padding-top : 70px;
            padding-botton : 30px;
        }
   	</style>
 
<title>구매정보 수정</title>

<!-- 
<script src="https://code.jquery.com/jquery-2.2.4.js" integrity="sha256-iT6Q9iMJYuQiMWNd9lDyBUStIq/8PuOW33aOqmvFpqI=" crossorigin="anonymous"></script>
 -->
<script type="text/javascript">

	function checkNull() {
		let divyDate = document.querySelector('input[name="divyDate"]').value;
		if(divyDate === '') {
			alert("배송 희망 일자는 필수 입력 사항입니다.");
			return false;
		}
		return true;
	}

	function fncUpdatePurchase() {
		if (checkNull() === true) {
			document.updatePurchase.submit();
		}
			
	}
	
	$( function() {
		
		$('button:contains("수정")').on('click', function() {
			// $(window.parent.frames['rightFrame'].document.location).attr('href', 'javascript:fncUpdatePurchase()');
			fncUpdatePurchase();
		}).on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});
		
		$('button:contains("취소")').on('click', function() {
			// $(window.parent.frames['rightFrame'].document.location).attr('href', 'javascript:history.go(-1)');
			history.go(-1);
		}).on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});
	});
</script>

  <!-- jQuery UI calendar -->
  <!-- 주의 :: 중복되는 script를 추가하면 not a function 오류가 발생한다. -->
   <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.12.4.js"></script>
  <script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

		<script>
		   $(function() {
		       //input을 datepicker로 선언
		       $("#divyDate").datepicker({
		           dateFormat: 'yy-mm-dd' //달력 날짜 형태
		           ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
		           ,showMonthAfterYear:true // 월- 년 순서가아닌 년도 - 월 순서
		           ,changeYear: true //option값 년 선택 가능
		           ,changeMonth: true //option값  월 선택 가능                
		           ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
		           ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
		           ,buttonImageOnly: true //버튼 이미지만 깔끔하게 보이게함
		           ,buttonText: "선택" //버튼 호버 텍스트              
		           ,yearSuffix: "년" //달력의 년도 부분 뒤 텍스트
		           ,monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 텍스트
		           ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip
		           ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 텍스트
		           ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 Tooltip
		           ,minDate: "-5Y" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
		           ,maxDate: "+5y" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)  
		       });                    
		       
		       //초기값을 오늘 날짜로 설정해줘야 합니다.
		       $('#datepicker').datepicker('setDate', 'today'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, -1M:한달후, -1Y:일년후)            
		   });
		</script>

</head>

<body bgcolor="#ffffff" text="#000000">

	<!-- ToolBar Start /////////////////////////////////////-->
	<jsp:include page="/layout/toolbar.jsp" />
   	<!-- ToolBar End /////////////////////////////////////-->



<div class="container">
		<div class="page-header">
		  <h2>구매 정보 수정</h2>
		</div>
</div>

<div class='container'>
	<form name="updatePurchase" method="post"	action="/purchase/updatePurchase?tranNo=${purchase.tranNo }">
	
	<!-- name을 OGNL에 맞게 하면, spring container가 알아서 auto binding한다. -->
	<input type="hidden" name="purchaseProd.prodNo" value="${purchase.purchaseProd.prodNo}" />
	<input type="hidden" name="buyer.userId" value="${purchase.buyer.userId }" />
	<input type="hidden" name="orderDate" value="${purchase.orderDate }" />
	
	
		<div class='form-group'>
			<label for='buyerId'>구매자아이디</label>
			<input class='form-control' type='text' placeholder='${purchase.buyer.userId}' disabled>
		</div>
		
		<div class='form-group'>
			<label for='paymentOption'>구매방법</label>
			<select 	name="paymentOption"		class="form-control" >
				<option value="1" <c:if test="${purchase.paymentOption == 1 }"> selected="selected" </c:if>>현금구매</option>
				<option value="2" <c:if test="${purchase.paymentOption == 2 }"> selected="selected" </c:if>>신용구매</option>
			</select>
		</div>
		
		<div class='form-group'>
			<label for='receiverName'>구매자이름</label>
			<input class='form-control' type='text' name='receiverName' placeholder='구매자 본인의 이름을 입력해주세요.' value="${purchase.receiverName }">
		</div>
		
		<div class='form-group'>
			<label for='receiverPhone'>구매자연락처</label>
			<input class='form-control' type='text' name='receiverPhone' placeholder='"-"를 포함하여 입력해주세요.' value="${purchase.receiverPhone }">
		</div>
		
		<div class='form-group'>
			<label for='divyAddr'>구매자주소</label>
			<input class='form-control' type='text' name='divyAddr' placeholder='구매자 주소를 입력해주세요.' value="${purchase.divyAddr }" >
		</div>
		
		<div class='form-group'>
			<label for='divyRequest'>구매요청사항</label>
			<input class='form-control' type='text' name='divyRequest' placeholder='구매 요청사항을 입력해주세요.'  value="${purchase.divyRequest }">
		</div>
		
	  <div class="form-group">
	  	<label for="numberOfPurchase">수량</label>
	  	<input type="number" class="form-control" name="numberOfPurchase" id="numberOfPurchase" placeholder="수정할 수량을 입력해주세요" value = "${purchase.numberOfPurchase }">
	  </div>

		 <div class='form-group'>
		 	<label for='divyDate'>배송희망일자</label>
		 	<input name='divyDate' id="divyDate" type="text" value="${purchase.divyDate	 }">
		 </div>
		 
		 
	</form>
	
	<div class="row">
		<div class="col-sm-offset-10">
			<button type="button" class="btn btn-default">수정</button>
		  	<button type="button" class="btn btn-default">취소</button>
		</div>
	</div>
</div> <!-- container end -->
</body>
</html>