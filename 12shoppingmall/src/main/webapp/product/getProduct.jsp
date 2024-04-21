<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

	<!--  ///////////////////////// CSS ////////////////////////// -->
	<style>
		body {
            padding-top : 50px;
            padding-bottom : 30px;
        }
    </style>


<title>상품 상세 조회</title>

<script src="https://code.jquery.com/jquery-2.2.4.js" integrity="sha256-iT6Q9iMJYuQiMWNd9lDyBUStIq/8PuOW33aOqmvFpqI=" crossorigin="anonymous"></script>
<script  type="text/javascript">

	$( function() {
		
		const prodNo = ${requestScope.product.prodNo };
		console.log(prodNo);
		$('button:contains("구매")').on('click', function() {
			// $(window.parent.frames["rightFrame"].document.location).attr('href', '/purchase/addPurchase?prodNo=${product.prodNo}');  // JSP는 적용 가능. 하지만 JSTL은 적용 불가...
			
			// 보안을 위해 DB에서 직접 재고 개수 확인하자. (view 값을 긁어오는 것은 취약함)
			// 어차피 정적인 data를 전달할 것이므로 JSTL 사용 해도 문제없다.
			
			$.ajax({
				
				url : "/rest/product/checkInventory",
				method : "POST",	
				headers : {
					"Accept" : "text/plain",
					"Content-Type" : "text/plain"
				},
				dataType : "text",
				data : "${requestScope.product.prodNo }",
				success : function(data) {
					
					if( data == "0") {
						alert("현재 판매 가능한 상품이 없어서 구매가 불가능합니다...");
						return ;
					}
					else
						// 여기도 보안취약하니까 여기서도 DB 조회 작업해야 할듯.
						window.location.href = "/purchase/addPurchase?prodNo=${product.prodNo}";
				}
			});
		}).on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});
		
		$('button:contains("이전")').on('click', function() {
			// $(window.parent.frames["rightFrame"].document.location).attr('href', 'javascript:history.go(-1)');  // 반드시 'javascript:'를 명시해주어야 한다.
			
			// 새 창이 아니면 이전 page로
			if(window.history.length == 0) {
				history.go(-1);
			} else {
				// 이전 page url로 이동
				window.location.href = document.referrer;
			}
		}).on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});
		
		$('button:contains("장바구니")').on('click', function() {
			
			// REST 통신으로 장바구니 list에 추가
			$.ajax({
				
				url : "/rest/purchase/addShoppingCart",
				method:"POST",
				headers : {
					"Accept" : "application/json",
					"Content-Type" : "application/json"
				},
				dataType : "JSON",
				data : JSON.stringify({
					prodNo : "${requestScope.product.prodNo}",
					userId : "${sessionScope.user.userId}",
					prodName : "${product.prodName}"
				}),
				success : function(responseBody, httpStatus) {
					
					alert(responseBody.msg);
				}
			});
		}).on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});
	});

</script>

</head>

<body bgcolor="#ffffff" text="#000000">

<script>
console.log("window.location.href = " + window.location.href);
console.log("document.referrer = " + document.referrer);
</script>

	<!-- ToolBar Start /////////////////////////////////////-->
	<jsp:include page="/layout/toolbar.jsp" />
   	<!-- ToolBar End /////////////////////////////////////-->

	<div class="container">
		<div class="page-header">
			<h2>
				상품상세조회
			</h2>
		</div>
		
		
	<table class="table">
		<tbody>
			<tr>
				<th scope="row">상품번호</th>
				<td>${product.prodNo }</td>
			</tr>
			<tr>
				<th scope="row">상품명</th>
				<!-- object scope 종류 :: pageScope < requestScope < sessionScope < applicationScope -->
				<td>${requestScope.product.prodName }</td>
			</tr>
			<tr>
				<th scope="row">상품이미지</th>
				<td>
					<%--
						이유는 모르겠으나, Spring boot 변경 후 folder 구조가 바뀌었는데도 기존 경로로도 유효하게 작동함.
						Spring boot가 동적으로 처리하는 것으로 추측됨. (변경 직후 최초에는 적용이 안됐거든.)
					 --%>
					<img src="/images/uploadFiles/${product.fileName }" style="max-width : 400px; max-height : 300px;" align="absmiddle" />
				</td>
			</tr>
			<tr>
				<th scope="row">상품상세정보</th>
				<td>${product.prodDetail }</td>
			</tr>
			<tr>
				<th scope="row">제조일자</th>
				<td>${product.manuDate }</td>
			</tr>
			<tr>
				<th scope="row">가격</th>
				<td>${product.price }</td>
			</tr>
			<tr>
				<th scope="row">등록일자</th>
				<td> ${product.regDate }</td>
			</tr>
			<tr>
				<th scope="row">재고</th>
				<td id="remains"> ${product.inventory }</td>
			</tr>
		</tbody>
	</table>
	
	<div class="row">
		<button type="button" class="btn btn-default col-sm-offset-9">구매</button>
		<button type="button" class="btn btn-default">장바구니에 담기</button>
		<button type="button" class="btn btn-default">이전</button>
	</div>
</div> <!-- container end -->

</body>
</html> 