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
   	
   		$(function(){
   			
   			// checked�� �Ǹ� �� �ݾ� ����ؼ� ����
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
	
		<table class='table table-striped'>
		<thead>
			<th>����</th>
			<th>��ǰID</th>
			<th>��ǰ��</th>
			<th>���ż���</th>
			<th>�ݾ�</th>
		</thead>
		<tbody>
			<c:forEach var="item" items="${requestScope.list }">
			<tr>
				<th scope='row'>
					<div>
						<input class="checkbox" type="checkbox">
					</div>
				</th>
				<td><a href="/product/getProduct/search?prodNo=${item.prodNo }">${item.prodNo }</a></td>
				<td>${item.prodName }</td>
				<!-- checkbox�� ������ �����ϹǷ�, ������ class�� ���� JQuery array�� ���� ������ selector ����Ͽ� �����غ���. -->
				<td class="numberOfPurchase"></td>
				<td class="price"></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	
	<div class="container">
		
		<!-- ���� �Ź��� ���� �� �ݾ� ���� ��� :: javascript �ʿ� -->
		<div class="row">
			<h1 class="col-md-offset-9">�� �ݾ�</h1>
		</div>
		<div class="row">
			<h2 class="col-md-offset-9 col-md-2" style="color:blue;">10000000</h2>
			<h2 class="col-md-1"> ��</h2>
		</div>
	</div>
</body>
</html>