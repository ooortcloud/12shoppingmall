<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>장바구니</title>
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
   
   
   <!-- jQuery UI toolTip 사용 CSS-->
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <!-- jQuery UI toolTip 사용 JS-->
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  
  	<!--  CSS 추가 : 툴바에 화면 가리는 현상 해결 :  주석처리 전, 후 확인-->
	<style>
        body {
            padding-top : 50px;
            padding-botton : 30px;
        }
   	</style>
   	
   	<script type="text/javascript">
   	
   		$(function(){
   			
   			// checked가 되면 총 금액 계산해서 변경
   			$('input[type="checkbox"]').on('change', function() {
   				console.log('flag');
   			})
   		});
   	</script>
   	
</head>
<body>

	<jsp:include page="/layout/toolbar.jsp"/>
	
	<div class="container">
		<div class="page-header">
			<h2>장바구니 목록</h2>
		</div>
	</div>
	
	<!-- table 형식 view -->
	<!-- check box를 통한 선택적 구입 필요 -->
	<!-- 구매 개수를 장바구니 내에서 자유롭게 조정 가능하게 만들어야 함 -->
	<!-- 총 금액이 잘 보이도록 구성 -->
	
	<!-- getProduct page에서 장바구니에 담기 기능 append -->
	<!-- shoppingcart 전용 구매 page 필요 (기존 page 활용) -->
	<!-- addPurchase 여러 번 날리는 것으로 끝내자. (batch를 사용해 최적화 가능함) -->
		
	<div class="container">
	
		<table class='table table-striped'>
		<thead>
			<th>선택</th>
			<th>상품ID</th>
			<th>상품명</th>
			<th>구매수량</th>
		</thead>
		<tbody>
			<c:forEach var="item" items="${requestScope.list }">
			<tr>
				<th scope='row'>
					<div>
						<input class="checkbox" type="checkbox">
					</div>
				</th>
				<td><a href="/product/getProduct/search?prodNo=${purchase.purchaseProd.prodNo }">${purchase.purchaseProd.prodNo }</a></td>
				<td>${purchase.purchaseProd.prodName }</td>
				<td>${purchase.numberOfPurchase }</td>
				<td>${purchase.orderDate }</td>
				<%-- tran_state_code : "1" = "구매완료", "2" = "배송중", "3" = "배송완료" --%>
				<td id="${purchase.tranNo }">
					<c:if test="${ purchase.tranCode == 1 }">
						현재 구매완료 상태입니다.
					</c:if><c:if test="${ purchase.tranCode == 2 }">
						현재 배송중 입니다. 
					</c:if><c:if test="${ purchase.tranCode == 3 }">
						현재 배송완료 상태 입니다.
					</c:if>
				</td>
				<td>
					<c:if test="${purchase.tranCode == 1 }">
						<!-- <span id="modifier1">구매 정보 확인 및 수정</span>  -->
						<a href="/purchase/getPurchase?tranNo=${purchase.tranNo }">구매 정보 확인 및 수정</a> 
					</c:if><c:if test="${purchase.tranCode == 2 }">
						<!-- <span id="modifier2">물건 도착 알리기</span>  -->
						<%--<a href="/purchase/updateTranCode?tranNo=${purchase.tranNo }&tranCode=3">물건 도착 알리기</a>  --%>
						<span id="${purchase.tranNo }" onclick="notifyArrival(event)">물건 도착 알리기</span>
						<p>   
					</c:if>
				</td>
			</tr>
			<c:set var="num" value="${ num-1 }" />
		</c:forEach>
		</tbody>
	</table>
	</div>
	
	<div class="container">
		
		<!-- 선택 매물에 따른 총 금액 동적 계산 :: javascript 필요 -->
		<div class="row">
			<h1 class="col-md-offset-9">총 금액</h1>
		</div>
		<div class="row">
			<h2 class="col-md-offset-9 col-md-2" style="color:blue;">10000000</h2>
			<h2 class="col-md-1"> 원</h2>
		</div>
	</div>
</body>
</html>