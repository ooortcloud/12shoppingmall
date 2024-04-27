/*
function reSizeImg(border, image) {
	
	const div = border; // 이미지를 감싸는 div
	const img = image; // 이미지
	const divAspect = 200 / 243; // height / width [px]
	const imgAspect = img.height / img.width;
	
	if (imgAspect <= divAspect) {
	    // 이미지가 div보다 납작한 경우 세로를 div에 맞추고 가로는 잘라낸다
	    let imgWidthActual = div.offsetHeight / imgAspect;
	    let imgWidthToBe = div.offsetHeight / divAspect;
	    let marginLeft = -Math.round((imgWidthActual - imgWidthToBe) / 2);
	    img.style.cssText = 'width: auto; height: 100%; margin-left: '
	                      + marginLeft + 'px;'
	} else {
	    // 이미지가 div보다 길쭉한 경우 가로를 div에 맞추고 세로를 잘라낸다
	    img.style.cssText = 'width: 100%; height: auto; margin-left: 0;';
	}
}
*/


function reSizeImg(img) {
	
	const borderWidth = 243;
	const borderWidthCenter = borderWidth/2;
	const imgWidthCenter = img.width()/2;  // css('width')는 [px]이 붙은 문자열이어서, 숫자 연산이 불가능함. 반드시 width() 사용.
	
	const movePixel = imgWidthCenter - borderWidthCenter;
		
	img.css('transform', 'translateX(-'+movePixel+'px)');
	console.log( img.css('transform') );
}


