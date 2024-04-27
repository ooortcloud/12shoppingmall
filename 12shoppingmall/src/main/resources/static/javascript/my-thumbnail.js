
function reSizeImg(img) {
	
	const borderWidth = 243;
	const imgWidth = img.width();
	
	if(borderWidth < imgWidth) {
		const borderWidthCenter = borderWidth/2;
		const imgWidthCenter = imgWidth/2;  // css('width')는 [px]이 붙은 문자열을 return해서 숫자 연산이 불가능함. 반드시 width() 사용.
		const movePixel = imgWidthCenter - borderWidthCenter;
		img.css('transform', 'translateX(-'+movePixel+'px)');	
		img.css('transform', 'translateX(-'+movePixel+'px)');
	} 
	// border 내 여백이 생기는 경우 다시 비율을 재조정 
	// 처음부터 분기문을 통해 비율 조정을 1회만 하면 되지 않겠느냐 생각할 수 있겠지만, 어차피 width 계산을 하려면 결국 비율 계산 수행해야 함. 
	// 그럴바에 computer에게 연산시킨 값을 보고 다시 재처리하는 것이 저렴하다고 판단함.
	else {
		// 비율 재조정
		img.css('max-height', 'none');
		img.css('max-width', '243px');	
		
		
		// const movePixel = imgWidthCenter - borderWidthCenter;
		// img.css('transform', 'translateX(-'+movePixel+'px)');
	}
}

/* 어떻게 하면 transform을 동적으로 멀티로 주입할 수 있을까.
$(function(){
	
	$('.my-thumbnail').on('mouseover', function() {
		$(this).css('transform', 'scale(1.02)');
	}).on('mouseout', function() {
		$(this).css('transform', 'scale(1)');
	});
});
*/