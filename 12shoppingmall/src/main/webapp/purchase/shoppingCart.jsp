<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
    
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
   	
   		function calculateSum() {
   			
   			const arr = $('tr');
				let sum = 0;
				// table header(?)에도 <tr> tag가 반영되므로 제외하기 위함
				for(let i = 1; i < arr.length; i++) {
					
					const row = $(arr[i]);
					const price = parseInt($('#numberOfPurchase'+i).val()) * parseInt($('#priceVal'+i).text());
					
					row.find('span').text( price );
					
					if(row.find('input:checkbox').is(':checked')) {
						sum += price;
					}
				}
				
			$('#totalPrice').text(sum);
   		}
   		
   		function checkInventory(prodNo) {

   			return new Promise((resolve,reject) => {

   				$.ajax({
   					
   					url : "/rest/product/checkInventory",

   					method : "POST",
	
   					dataType : "text",

   					headers : {

	   					"Accept" : "text/plain",
	
	   					"Content-Type" : "text/plain"

   					},
   					data : prodNo,
   					success : function(numberOfInventory) {

   						resolve(numberOfInventory);
   					}
   				});
			});
   		}
   	</script>
   	
   	<script type="text/javascript">
   	
   		/* 계속 조건 비교해야 하는 것은 너무 비효율적.
   		$(function(){
   			
   			// 개수를 수정할 때마다 총 금액 계산에 반영
   			$('input[type="number"]').on('change', function() {
   				
   				// jQuery array enhanced for loop
   				$.each($('tr'), function(index, value) {
   					
   					// table header(?)에도 <tr> tag가 반영되므로 제외하기 위함
   					if(index > 0) {
   						console.log(index +" : " + $(value).html());
   					}
   				});
   			})
   		});
   		*/
   		
   		$(function() {
   			
   			// 개수를 수정할 때마다 총 금액 계산에 반영
   			$('input[type="number"]').on('change', function(event) {
   				
   				// '구매' button을 누를 때, 사용자가 임의로 input 값 변경이 가능한가?
   				const numberOfPurchase = $(event.target).val();
   				if(numberOfPurchase < 0) {
   					alert('구매 수량은 반드시 0 이상이어야 합니다.');
   					$(event.target).val(0);
   				}

   				// 현재 재고보다 많은 수량을 요구하는 경우 deny
   				const prodNo = $($($(event.target).parents()[2]).find("td")[0]).text();
   				checkInventory(prodNo)
   				.then(function(inventory){
					
   					// 최대 개수를 넘지 못하게 보정
   					if(parseInt(inventory) < parseInt(numberOfPurchase)) {
   						alert("현재 최대 재고는 "+inventory+" 개 입니다.");
   						$(event.target).val(inventory);
   					}
   					
   					calculateSum();
   				}).catch(function(err) {
   					// error 객체를 만들지 않았으니 불필요하긴 함. 하지만 reference를 위해 작성함.
   					console.log("fail");
   				});
   			});
   			
   			$('input[type="checkbox"]').on('change', function() {
   				calculateSum();
   			})
   		});
   		
   		$(function() {
   			
   			$('button:contains("삭제")').on('click', function(event) {
   				
   				// 사용자가 임의로 hidden data를 변경할 수 있을까?
   				const number = $(event.target).parents().parents().attr('id');
   				const cartNo = $('#cartNo'+number).text();
   				
   				console.log($('#'+number).html());
   				$.ajax({
   					
   					url : "/rest/purchase/deleteShoppingCartItem",
					method : "POST",
					headers : {
						"Accept" : "application/json",
						"Content-Type" : "application/json"
					},
					dataType :"JSON",
					data : JSON.stringify({
						cartNo : cartNo					
					}),
					success : function(responseBody, httpStatus) {
						
						if(responseBody.msg == "ok") {
							$('#'+number).remove()
						} else {
							alert(responseBody.msg);
						}
						
					}
   				});
   			})
   		});
   		
   		$(function() {
   			
   			$('button:contains("구매")').on('click', function() {
   				
   				const checkboxes = $('input[type="checkbox"]');
   				const allChecked = checkboxes.filter(':checked').length === 0;

   				if (allChecked) {
   				  alert('하나 이상의 상품을 선택해주세요.');
   				  return false;
   				}   				
   				
   				const form = $('#shoppingCartList');
   				
   				
   				form.attr('action', "/purchase/checkOutOfTheCart");
   				form.attr('method', "post");
   				form.submit();
   			});
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
	
		<form id="shoppingCartList">
		<table class='table table-striped'>
		<thead>
			<th>선택</th>
			<th>상품ID</th>
			<th>상품명</th>
			<th>구매수량</th>
			<th>금액</th>
			<th>삭제</th>
		</thead>
		<tbody>
			<!-- num 기반 id 동적 처리를 해보자. -->
			<c:set var="num" value="1" />
			<c:forEach var="item" items="${requestScope.list }">
			<!-- 상우 tag 요소에 접근하여 numbering을 쉽게 처리 -->
			<tr id="${num }">
				<th scope='row'>
					<div>
						<%-- 보안 상 안전한 방식인가? --%>
						<input class="checkbox" type="checkbox" name="selected" value="${item.prodNo} ${num-1} ${item.cartNo}">
					</div>
				</th>
				<td><a href="/product/getProduct/search?prodNo=${item.prodNo }">${item.prodNo }</a></td>
				<td>${item.prodName }</td>
				<!-- checkbox의 순서와 동일하므로, 동일한 class에 대한 JQuery array에 대해 순서로 selector 사용하여 접근해보자. -->
				<td class="numberOfPurchase">
					<div class="form-group">
						<input type="number" class="form-control" id="numberOfPurchase${num }" name="numberOfPurchase" style="width:150px" value="0" >
						<label for="numberOfPurchase${num }">(현재 재고 : ${item.inventory } 개)</label>	
					</div>				
				</td>
				<td class="price" id="priceView${num }">
					<span>0</span> 원
					<p id="priceVal${num }" style="display:none;">${item.price }</p>	
				</td>
				<td>
					<button type="button" class="btn btn-default" id="delete${num }">삭제</button>
					<p id="cartNo${num }" style="display:none;">${item.cartNo }</p>	
				</td>
				
			</tr>
			
			<c:set var="num" value="${num + 1 }" />
			</c:forEach>
		</tbody>
		</table>
		</form>
	</div>
	
	<div class="container">
		
		<!-- 선택 매물에 따른 총 금액 동적 계산 :: javascript 필요 -->
		<div class="row">
			<h1 class="col-md-offset-9">총 금액</h1>
		</div>
		<div class="row">
			<h2 class="col-md-offset-9 col-md-2" style="color:blue;" id="totalPrice">0</h2>
			<h2 class="col-md-1"> 원</h2>
		</div>
		<br/>
		<div class="row">
			<button type="button" class="btn btn-default col-md-offset-11">구매하기</button>
		</div>
	</div>
</body>
</html>