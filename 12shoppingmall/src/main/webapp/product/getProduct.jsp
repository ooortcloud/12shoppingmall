<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>

<!-- <link rel="stylesheet" href="/css/admin.css" type="text/css">  -->


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

	<!--  ///////////////////////// CSS ////////////////////////// -->
	<style>
		body {
            padding-top : 50px;
            padding-bottom : 30px;
        }
    </style>


<title>��ǰ �� ��ȸ</title>

<script src="https://code.jquery.com/jquery-2.2.4.js" integrity="sha256-iT6Q9iMJYuQiMWNd9lDyBUStIq/8PuOW33aOqmvFpqI=" crossorigin="anonymous"></script>
<script  type="text/javascript">

	$( function() {
		
		$('button:contains("����")').on('click', function() {
			// $(window.parent.frames["rightFrame"].document.location).attr('href', '/purchase/addPurchase?prodNo=${product.prodNo}');  // JSP�� ���� ����. ������ JSTL�� ���� �Ұ�...
			window.location.href = "/purchase/addPurchase?prodNo=${product.prodNo}";
		}).on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});
		
		$('button:contains("����")').on('click', function() {
			// $(window.parent.frames["rightFrame"].document.location).attr('href', 'javascript:history.go(-1)');  // �ݵ�� 'javascript:'�� ������־�� �Ѵ�.
			
			// �� â�� �ƴϸ� ���� page��
			if(window.history.length == 0) {
				history.go(-1);
			} else {
				// ���� page url�� �̵�
				window.location.href = document.referrer;
			}
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
				��ǰ����ȸ
			</h2>
		</div>
		
		
	<table class="table">
		<tbody>
			<tr>
				<th scope="row">��ǰ��ȣ</th>
				<td>${product.prodNo }</td>
			</tr>
			<tr>
				<th scope="row">��ǰ��</th>
				<!-- object scope ���� :: pageScope < requestScope < sessionScope < applicationScope -->
				<td>${requestScope.product.prodName }</td>
			</tr>
			<tr>
				<th scope="row">��ǰ�̹���</th>
				<td>
					<img src="/images/uploadFiles/${product.fileName }" style="max-width : 400px; max-height : 300px;" align="absmiddle" />
				</td>
			</tr>
			<tr>
				<th scope="row">��ǰ������</th>
				<td>${product.prodDetail }</td>
			</tr>
			<tr>
				<th scope="row">��������</th>
				<td>${product.manuDate }</td>
			</tr>
			<tr>
				<th scope="row">����</th>
				<td>${product.price }</td>
			</tr>
			<tr>
				<th scope="row">�������</th>
				<td> ${product.regDate }</td>
			</tr>
		</tbody>
	</table>
	
	<div class="row">
		<button type="button" class="btn btn-default col-sm-offset-10">����</button>
		<button type="button" class="btn btn-default">����</button>
	</div>
</div> <!-- container end -->

</body>
</html> 