package com.model2.mvc.common.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/*
 * FileName : PojoAspectJ.java
 *	:: XML �� ���������� aspect �� ����   
  */
@Aspect  // Spring AOP ����
@Component
public class LogAspectJ {

	///Constructor
	public LogAspectJ() {
		System.out.println("\nCommon :: "+this.getClass()+" default constructor...\n");
	}
	
	//Around  Advice
	@Around("execution(* com.model2.mvc.service..*.*(..) )")  // join point�� around�� ����, point-cut ����
	public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
			
		System.out.println("");
		System.out.println("[Around before] target object's method :: "+
													joinPoint.getTarget().getClass().getSimpleName() +"."+
													joinPoint.getSignature().getName());
		if(joinPoint.getArgs().length !=0){
			System.out.println("[Around before] method�� ���޵Ǵ� argument ���� = " + joinPoint.getArgs().length);
			// array�� List�� �����Ͽ� toString() ���
			System.out.println("[Around before] method�� ���޵Ǵ� argument :: " + Arrays.asList(joinPoint.getArgs()) ); 
		}
		//==> Ÿ�� ��ü�� Method �� ȣ�� �ϴ� �κ� 
		System.out.println(getClass().getSimpleName() +" :: target  object's  method proceed... ");
		Object obj = joinPoint.proceed();

		System.out.println("[Around after] '"+joinPoint.getTarget().getClass().getSimpleName() +"."+joinPoint.getSignature().getName()+"' return value = "+obj);
		System.out.println("");
		
		return obj;
	}
	
	// Spring AOP�� constructor �޼ҵ忡 �������� ���Ѵ�...
	/*
	public void logConstructor(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println(getClass().getSimpleName() + " default constructor...");
		joinPoint.proceed();
	}
	*/
	
}//end of class