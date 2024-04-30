<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>

<html>
<head>
<title>��ǰ���</title>

<!-- <link rel="stylesheet" href="/css/admin.css" type="text/css"> -->

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
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <!-- jQuery UI toolTip ��� JS-->
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	
	<!--  CSS �߰� : ���ٿ� ȭ�� ������ ���� �ذ� :  �ּ�ó�� ��, �� Ȯ��-->
	<style>
        body {
            padding-top : 70px;
            padding-botton : 30px;
        }
   	</style>

<script type="text/javascript" src="../javascript/calendar.js">
</script>

<script type="text/javascript">

function fncAddProduct(){
	//Form ��ȿ�� ����
 	var name = document.detailForm.prodName.value;
	var detail = document.detailForm.prodDetail.value;
	var manuDate = document.detailForm.manuDate.value;
	var price = document.detailForm.price.value;

	if(name == null || name.length<1){
		alert("��ǰ���� �ݵ�� �Է��Ͽ��� �մϴ�.");
		return;
	}
	if(detail == null || detail.length<1){
		alert("��ǰ�������� �ݵ�� �Է��Ͽ��� �մϴ�.");
		return;
	}
	if(manuDate == null || manuDate.length<1){
		alert("�������ڴ� �ݵ�� �Է��ϼž� �մϴ�.");
		return;
	}
	if(price == null || price.length<1){
		alert("������ �ݵ�� �Է��ϼž� �մϴ�.");
		return;
	}

	document.detailForm.action='/product/addProduct';    

	document.detailForm.submit();
}


$( function() {
	
	$('button:contains("���")').on('click', function() {
		fncAddProduct();
	});
	
	$('button:contains("�ʱ�ȭ")').on('click', function() {
		document.detailForm.reset();
	});
	
	$('button:contains("���")').on('click', function() {
		window.location.href="/product/listProduct/manage";
	});
	
	$('input:file:last').on('change', function(event) {

		imgs = event.target.files;
		console.log(imgs);
		if (imgs.length >= 4) {
			alert('������ �ִ� 3�� ��� �����մϴ�.');
			// event�� call by value�� �� �����غ��� ���ǹ���.
			$('input:file:last').val([]);  // �� array�� �ʱ�ȭ
		}
	});
})


</script>

</head>

<body bgcolor="#ffffff" text="#000000">

	<!-- ToolBar Start /////////////////////////////////////-->
	<jsp:include page="/layout/toolbar.jsp" />
   	<!-- ToolBar End /////////////////////////////////////-->



<div class="container">
		<div class="page-header">
		  <h2>��ǰ���</h2>
		</div>

<!-- file upload�� ���� encode type ���� -->
<form name="detailForm" method="post" enctype="multipart/form-data">
	
		  <div class="form-group">
		    <label for="prodName">��ǰ��</label>
		    <!-- form data�� �ݵ�� name�� ��ƾ� @ModelAttribute���� �ν���... -->
		    <input type="text" class="form-control" id="prodName" name="prodName" placeholder="��ǰ��">
		  </div>
		  <div class="form-group">
		    <label for="prodDetail">��ǰ������</label>
		    <input type="text" class="form-control" id="prodDetail" name="prodDetail" placeholder="��ǰ������">
		  </div>
	  	  <div class="form-group">
		    <label for="manuDate">��������</label>
		    <input type="text" name="manuDate" readonly="readonly" class="ct_input_g"  
							style="width: 100px; height: 19px"	maxLength="10" minLength="6"/>
					&nbsp;<img src="../images/ct_icon_date.gif" width="15" height="15" 
											onclick="show_calendar('document.detailForm.manuDate', document.detailForm.manuDate.value)"/>
		  </div>
		  <div class="form-group">
		    <label for="price">����</label>
		    <label class="sr-only" for="price">����</label>
		    <div class="input-group">
		      <input type="number" class="form-control" id="price" name="price" placeholder="����">
		      <div class="input-group-addon">��</div>
	    	</div>
		  </div>
		  <div class="form-group">
		  	<label for="inventory">����</label>
		  	<input type="number" class="form-control" name="inventory" id="inventory" placeholder="�ʱ� ������ �Է����ּ���">
		  </div>
		  <div class="form-group">
		    <label for="thumbnail">�����</label>
		    <input type="file" id="thumbnail" name="thumbnail">
		    <p class="help-block">�ִ� 5MB ���ϸ� �����մϴ�...</p>
		  </div>
		<div class="form-group">
			<div>
				<label for="productImages">��ǰ ���� �̹�����</label>
				<input type="file" name="productImages" accept="image/*" multiple>
			</div>
		</div>
		  
		  <div class="row">
		  	<div class="col-sm-offset-10">
			  <button type="button" class="btn btn-default">���</button>
	
				<button type="button" class="btn btn-default">�ʱ�ȭ</button>
		
				<button type="button" class="btn btn-default">���</button>
			</div>
		</div>
</form>
</div>
</body>
</html>