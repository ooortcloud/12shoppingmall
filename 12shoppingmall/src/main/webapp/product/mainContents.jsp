<%@ page contentType="text/html; charset=EUC-KR" %>
<%@ page pageEncoding="EUC-KR"%>


<!--  ///////////////////////// JSTL  ////////////////////////// -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
	.my-thumbnail:hover {
		transform : scale(1.02);
		transition : .5s;
	}

</style>

<script>

	/// ���� scroll thumbnail set
	function setThumbnails(responseBody, httpStatus) {

		temp = "<div class='row'>";
		let i = 0;
		for(const product of responseBody.list ) {
			 
			if(i == 4) {
				temp += '</div>';
				temp += '<div class="row">';
			}
			temp += '<div class="col-sm-6 col-md-3">';
			temp += '<div class="thumbnail" style="height = 500px;" onclick=\' location.href = "/product/getProduct/search?prodNo='+product.prodNo+'" \'>';
			
			if(product.fileName == null || product.fileName =='null') {
				temp += '<img class="my-thumbnail" src="http://placeholder.com/243X200" />';
			} else {
				temp += '<img class="my-thumbnail" src="/images/uploadFiles/'+product.fileName+'" />';	
			}
			
			temp += '<div class="caption">';
			temp += '<h3>'+product.prodName+'</h3>';
			temp += '<p>'+product.price+'</p>';
			temp += '</div>';
			temp += '</div>';  <%-- thumbnail end --%>
			temp += '</div>';
			i++;
		}
		temp += '</div>';
		$('#searchList').append(temp);
		
		/// image ���� mouse �ø��� ǥ��
		// jQuery array�� ���� ���� ����
		const thumbnailArr = $('div.thumbnail');
		
		// ��� element ��ҵ鿡 ���� event ����
		thumbnailArr.on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});	
	}
	
	/// ���� scroll request
	let currentPage = 1;
	function getResources() {
		console.log("currentPage :: "+ currentPage );
		
		// root������ �˻� ���� element���� �������� �����Ƿ� ���� ó��
		if( location.href.substring(location.href.lastIndexOf("/")) == "/" ) {  // test
			$.ajax(	{
				
				url : "/rest/product/json/listProduct/search",
				method : "POST",
				headers : {
					"Accept" : "application/json",
					"Content-Type" : "application/json"
				},
				dataType : "JSON",
				// network ��� �ÿ��� ��ü�� �ƴ� string ���·� ��ȯ
				data : JSON.stringify({
					// currentPage : $('#tempCurrentPage').val()
					currentPage : currentPage
				}),
				success : function(responseBody, httpStatus) {
					currentPage = responseBody.resultPage.currentPage;
					setThumbnails(responseBody, httpStatus);
				}
			});
		} 
		// ��ǰ ���� page������ �˻� ������ �Ű��� ��
		else {
				$.ajax(	{
				
				url : "/rest/product/json/listProduct/search",
				method : "POST",
				headers : {
					"Accept" : "application/json",
					"Content-Type" : "application/json"
				},
				dataType : "JSON",
				// network ��� �ÿ��� ��ü�� �ƴ� string ���·� ��ȯ
				data : JSON.stringify({
					currentPage : currentPage,
					searchKeyword : $('#searchKeyword').val(),
					priceMin : $('#priceMin').val(),
					priceMax : $('#priceMax').val()
				}),
				success : function(responseBody, httpStatus) {
					currentPage = responseBody.resultPage.currentPage;
					setThumbnails(responseBody, httpStatus);
				}
			});
		}
	}

	/*
		<< ��� ������ px >>
		$(window).scrollTop() :: ���� browser�� ���� �������� view(=window) �� top(?)���� ��ġ�� get  == window.scrollY
		window.innerHeight :: ���� browser�� �������� view�� ����(?)
		$(document).height() :: ���� page�� �� height get (��� �߰��� ���� �������� ����)  == $(window).height() == document.body.scrollHeight
		$('body').height() :: body ���� ��ҵ��� �𿩼� �̷��� �� height get (��� �߰��� ���� �������� ����)  << style���� padding�� ������ ��
	*/
	// page ù load �� �̹� window height�� body height�� �Ѿ����, body height�� window height�� �Ѿ ������ resource get
	console.log("���� ��ҵ��� ������ �� ���� = " + $('body').height() );
	console.log("window ���� = " + $(window).height() );
	if ( $('body').height() < $(window).height() )
		getResources();
	console.log("\n");
	
		
	/// scroll event :: throttle pattern
	var throttleTimer = null;
	$(window).on('scroll', function() {

		if( !throttleTimer) {
			// ���� �ð����� event�� lock�Ǵ�.
			throttleTimer = setTimeout(function() {
				if (window.scrollY + window.innerHeight >= document.body.scrollHeight - 300) {
					getResources();
				}
				throttleTimer = null;
			}, 150);
		}
		
		console.log("\n");
	});

	
</script>

<div class="container"  id="searchList">

	<%--
	<div class="row">
	<c:forEach var="product" items="${list }" begin="0" end="3" step="1">

			<div class="col-sm-6 col-md-3">
				<div class="thumbnail" style="height = 500px;" onclick=' location.href = "/product/getProduct/search?prodNo=${product.prodNo}" '>
					<c:if test="${empty product.fileName }" >
						<img class="my-thumbnail" src="http://placeholder.com/243X200" />
								<!-- null ó���� �ݵ�� 'empty' keyword�� ����ؾ� �Ѵ�. -->		
					</c:if><c:if test='${ !empty product.fileName }' >

						<img class="my-thumbnail" src="/images/uploadFiles/${product.fileName }" />

					</c:if>
					<div class="caption">
						<h3>${product.prodName }</h3>
						<p>${product.price } ��</p>
					</div>
				</div> <!-- thumbnail end -->
			</div>

	</c:forEach>
	</div>
	
	<div class="row">
	<c:forEach var="product" items="${list }" begin="4" end="7" step="1">

			<div class="col-sm-6 col-md-3">
				<div class="thumbnail" style="height = 500px;" onclick=' location.href = "/product/getProduct/search?prodNo=${product.prodNo}" '>
					<c:if test="${empty product.fileName }" >
						<img class="my-thumbnail" src="http://placeholder.com/243X200" />
								<!-- null ó���� �ݵ�� 'empty' keyword�� ����ؾ� �Ѵ�. -->		
					</c:if><c:if test='${ !empty product.fileName }' >

							<img class="my-thumbnail" src="/images/uploadFiles/${product.fileName }"  />

					</c:if>
					<div class="caption">
						<h3>${product.prodName }</h3>
						<p>${product.price } ��</p>
					</div>
				</div> <!-- thumbnail end -->
			</div>

	</c:forEach>
	</div>
	 --%>
</div> <!-- container end -->