<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>��ٱ���</title>
	<!-- ���� : http://getbootstrap.com/css/   ���� -->
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
   
   
   <!-- jQuery UI toolTip ��� CSS-->
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <!-- jQuery UI toolTip ��� JS-->
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  
  	<!--  CSS �߰� : ���ٿ� ȭ�� ������ ���� �ذ� :  �ּ�ó�� ��, �� Ȯ��-->
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
				// table header(?)���� <tr> tag�� �ݿ��ǹǷ� �����ϱ� ����
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
   	
   		/* ��� ���� ���ؾ� �ϴ� ���� �ʹ� ��ȿ����.
   		$(function(){
   			
   			// ������ ������ ������ �� �ݾ� ��꿡 �ݿ�
   			$('input[type="number"]').on('change', function() {
   				
   				// jQuery array enhanced for loop
   				$.each($('tr'), function(index, value) {
   					
   					// table header(?)���� <tr> tag�� �ݿ��ǹǷ� �����ϱ� ����
   					if(index > 0) {
   						console.log(index +" : " + $(value).html());
   					}
   				});
   			})
   		});
   		*/
   		
   		$(function() {
   			
   			// ������ ������ ������ �� �ݾ� ��꿡 �ݿ�
   			$('input[type="number"]').on('change', function(event) {
   				
   				// '����' button�� ���� ��, ����ڰ� ���Ƿ� input �� ������ �����Ѱ�?
   				const numberOfPurchase = $(event.target).val();
   				if(numberOfPurchase < 0) {
   					alert('���� ������ �ݵ�� 0 �̻��̾�� �մϴ�.');
   					$(event.target).val(0);
   				}

   				// ���� ����� ���� ������ �䱸�ϴ� ��� deny
   				const prodNo = $($($(event.target).parents()[2]).find("td")[0]).text();
   				checkInventory(prodNo)
   				.then(function(inventory){
					
   					// �ִ� ������ ���� ���ϰ� ����
   					if(parseInt(inventory) < parseInt(numberOfPurchase)) {
   						alert("���� �ִ� ���� "+inventory+" �� �Դϴ�.");
   						$(event.target).val(inventory);
   					}
   					
   					calculateSum();
   				}).catch(function(err) {
   					// error ��ü�� ������ �ʾ����� ���ʿ��ϱ� ��. ������ reference�� ���� �ۼ���.
   					console.log("fail");
   				});
   			});
   			
   			$('input[type="checkbox"]').on('change', function() {
   				calculateSum();
   			})
   		});
   		
   		$(function() {
   			
   			$('button:contains("����")').on('click', function(event) {
   				
   				// ����ڰ� ���Ƿ� hidden data�� ������ �� ������?
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
   			
   			$('button:contains("����")').on('click', function() {
   				
   				const checkboxes = $('input[type="checkbox"]');
   				const allChecked = checkboxes.filter(':checked').length === 0;

   				if (allChecked) {
   				  alert('�ϳ� �̻��� ��ǰ�� �������ּ���.');
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
			<h2>��ٱ��� ���</h2>
		</div>
	</div>
	
	<!-- table ���� view -->
	<!-- check box�� ���� ������ ���� �ʿ� -->
	<!-- ���� ������ ��ٱ��� ������ �����Ӱ� ���� �����ϰ� ������ �� -->
	<!-- �� �ݾ��� �� ���̵��� ���� -->
	
	<!-- getProduct page���� ��ٱ��Ͽ� ��� ��� append -->
	<!-- shoppingcart ���� ���� page �ʿ� (���� page Ȱ��) -->
	<!-- addPurchase ���� �� ������ ������ ������. (batch�� ����� ����ȭ ������) -->
		
	<div class="container">
	
		<form id="shoppingCartList">
		<table class='table table-striped'>
		<thead>
			<th>����</th>
			<th>��ǰID</th>
			<th>��ǰ��</th>
			<th>���ż���</th>
			<th>�ݾ�</th>
			<th>����</th>
		</thead>
		<tbody>
			<!-- num ��� id ���� ó���� �غ���. -->
			<c:set var="num" value="1" />
			<c:forEach var="item" items="${requestScope.list }">
			<!-- ��� tag ��ҿ� �����Ͽ� numbering�� ���� ó�� -->
			<tr id="${num }">
				<th scope='row'>
					<div>
						<%-- ���� �� ������ ����ΰ�? --%>
						<input class="checkbox" type="checkbox" name="selected" value="${item.prodNo} ${num-1} ${item.cartNo}">
					</div>
				</th>
				<td><a href="/product/getProduct/search?prodNo=${item.prodNo }">${item.prodNo }</a></td>
				<td>${item.prodName }</td>
				<!-- checkbox�� ������ �����ϹǷ�, ������ class�� ���� JQuery array�� ���� ������ selector ����Ͽ� �����غ���. -->
				<td class="numberOfPurchase">
					<div class="form-group">
						<input type="number" class="form-control" id="numberOfPurchase${num }" name="numberOfPurchase" style="width:150px" value="0" >
						<label for="numberOfPurchase${num }">(���� ��� : ${item.inventory } ��)</label>	
					</div>				
				</td>
				<td class="price" id="priceView${num }">
					<span>0</span> ��
					<p id="priceVal${num }" style="display:none;">${item.price }</p>	
				</td>
				<td>
					<button type="button" class="btn btn-default" id="delete${num }">����</button>
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
		
		<!-- ���� �Ź��� ���� �� �ݾ� ���� ��� :: javascript �ʿ� -->
		<div class="row">
			<h1 class="col-md-offset-9">�� �ݾ�</h1>
		</div>
		<div class="row">
			<h2 class="col-md-offset-9 col-md-2" style="color:blue;" id="totalPrice">0</h2>
			<h2 class="col-md-1"> ��</h2>
		</div>
		<br/>
		<div class="row">
			<button type="button" class="btn btn-default col-md-offset-11">�����ϱ�</button>
		</div>
	</div>
</body>
</html>