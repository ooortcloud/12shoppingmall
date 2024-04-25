<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>

<%-- JSTL = JavaServerPage(JSP) Tag Library --%>
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
   
   
   <!-- jQuery UI toolTip ��� CSS-->
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <!-- jQuery UI toolTip ��� JS-->
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	
	<!--  CSS �߰� : ���ٿ� ȭ�� ������ ���� �ذ� :  �ּ�ó�� ��, �� Ȯ��-->
	<style>
        body {
            padding-top : 70px;
            padding-botton : 30px;
        }
   	</style>

<title>��ǰ �� ��ȸ</title>

<script type="text/javascript" src="../javascript/calendar.js">
</script>


<script type="text/javascript">

	function fncAddPurchase() {
		let divyDate = document.querySelector('input[name="divyDate"]').value;
		if(divyDate === '') {
			alert("��� ��� ���ڴ� �ʼ� �Է� �����Դϴ�.");
			return false;
		}
		
		let phoneNum = document.querySelector('input[name="receiverPhone"]').value;
		if( !phoneNum.includes('-')) {
			alert('�ݵ�� "-"�� �Բ� �Է����ּ���.');
			return false;
		}
		
		// �񵿱� ó���� ����� ��
		const numberOfPurchase = $('input[name="numberOfPurchase"]').val();
		if(numberOfPurchase <= 0) {
			alert("���� 1 �̻��� �ڿ������� �մϴ�.");
			return;
		}
		
		return new Promise( function(resolve, reject) {
			
			
			// �� ���ڿ��� false ���
			if (numberOfPurchase !== undefined && numberOfPurchase  ) {
				
				// ���� overflow ����
				$.ajax({
					
					url : "/rest/product/checkInventory",
					method : "Post",
					headers : {
						"Accept" : "text/plain",
						"Content-Type" : "text/plain"
					},
					dataType : "text",
					data : "${purchase.purchaseProd.prodNo }"
				}).then( function(data) { 
	
					console.log(parseInt(data) < numberOfPurchase);
					if(parseInt(data) < numberOfPurchase) {
						alert("���� ������ ���� ����� �����ϴ�. ���� ��� = " +data );
						reject(new Error());
					} else {
						document.addPurchase.submit();	
					}
				});
			} else {  
				alert("���� ������ �Է����ּ���.");
				return false;
			}
		});
	}
	
	
	/*
	function fncAddPurchase() {
		if (check() === true)
			document.addPurchase.submit();
	}
	*/

	
	$( function() {  
		 
		$('button:contains("����")').on('click', function() {
			// $(window.parent.frames["rightFrame"].document.location).attr('href', 'javascript:fncAddPurchase()');
			fncAddPurchase();
		}).on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});
		
		$('button:contains("���")').on('click', function() {
			// $(window.parent.frames["rightFrame"].document.location).attr('href', 'javascript:history.go(-1)');  // �ݵ�� 'javascript:'�� ������־�� �Ѵ�.\
			window.location.href="/product/listProduct/search";
		}).on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});
	});

</script>
</head>

<body>

	<!-- ToolBar Start /////////////////////////////////////-->
	<jsp:include page="/layout/toolbar.jsp" />
   	<!-- ToolBar End /////////////////////////////////////-->



<div class="container">
		<div class="page-header">
		  <h2>��ǰ �� ��ȸ</h2>
		</div>
</div>

<div class='container'>

	<form name="addPurchase" method="post" action="/purchase/addPurchase">
		<%-- hidden �Ӽ��� �̿��� form���� controller���� �۾� ���� �� �ʿ��� ���� ���� --%>
		<input type="hidden" name="prodNo" value="${purchase.purchaseProd.prodNo }" />
	
		<div class='form-group'>
			<label for='prodNo'>��ǰ ��ȣ</label>
			<input class='form-control' type='number' placeholder='${purchase.purchaseProd.prodNo }' disabled>
		</div>
	
		<div class='form-group'>
			<label for='prodName'>��ǰ��</label>
			<input class='form-control'type='text' placeholder='${purchase.purchaseProd.prodName }' disabled>
		</div>
		
		<div class='form-group'>
			<label for='prodDetail'>��ǰ �� ����</label>
			<input class='form-control' type='text' placeholder='${purchase.purchaseProd.prodDetail}' disabled>
		</div>
		
		<div class='form-group'>
			<label for='manuDate'>���� ����</label>
			<input class='form-control'  type='text' placeholder='${purchase.purchaseProd.manuDate}' disabled>
		</div>
	
		<div class='form-group'>
			<label for='manuDate'>����</label>
			<input class='form-control'  type='number' placeholder='${purchase.purchaseProd.price}' disabled>
		</div>
		
		<div class='form-group'>
			<label for='manuDate'>�������</label>
			<input class='form-control' type='text' placeholder='${purchase.purchaseProd.regDate}' disabled>
		</div>
		
		<div class='form-group'>
			<label for='buyerId'>�����ھ��̵�</label>
			<input class='form-control' type='text' placeholder='${purchase.buyer.userId}' disabled>
		</div>
		
		<div class='form-group'>
			<label for='paymentOption'>���Ź��</label>
			<select 	name="paymentOption"		class="form-control" >
				<option value="1" selected="selected">���ݱ���</option>
				<option value="2">�ſ뱸��</option>
			</select>
		</div>
		
		<div class='form-group'>
			<label for='receiverName'>�������̸�</label>
			<input class='form-control' type='text' name='receiverName' placeholder='������ ������ �̸��� �Է����ּ���.'>
		</div>
		
		<div class='form-group'>
			<label for='receiverPhone'>�����ڿ���ó</label>
			<input class='form-control' type='text' name='receiverPhone' placeholder='"-"�� �����Ͽ� �Է����ּ���.'>
		</div>
		
		<div class='form-group'>
			<label for='divyAddr'>�������ּ�</label>
			<input class='form-control' type='text' name='divyAddr' placeholder='������ �ּҸ� �Է����ּ���.'>
		</div>
		
		<div class='form-group'>
			<label for='divyRequest'>���ſ�û����</label>
			<input class='form-control' type='text' name='divyRequest' placeholder='���� ��û������ �Է����ּ���.'>
		</div>
		
		<div class='form-group'>
			<label for='divyDate'>����������</label>
			<input 	type="text" readonly="readonly" name="divyDate" class="ct_input_g" 
							style="width: 100px; height: 19px" maxLength="20"/>
			<img 	src="../images/ct_icon_date.gif" width="15" height="15"	
						onclick="show_calendar('document.addPurchase.divyDate', document.addPurchase.divyDate.value)"/>
		</div>
		
		<div class='form-group'>
			<label for='numberOfPurchase'>���� ����</label>
			<input class='form-control' type='number' name='numberOfPurchase' placeholder='������ ������ �Է����ּ���.'>
		</div>
	</form>
</div>  <!-- container end -->

<div class='container'>
	<div class='row'>
		<div class='col-sm-offset-10'>
			<button type='button' class='btn btn-default'>����</button>
			<button type='button' class='btn btn-default'>���</button>
		</div>
	</div>
</div> <!-- container end -->

</body>
</html>