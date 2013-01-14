package reflection;

import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class PropertyGenerator {
	private static String packageName = "com.refgen.";
	
	private static String conditionPrimitiveReturnFunc = "if( \"%s\".equals(prop) ) {" +
			"return %s.valueOf( ( (%s)obj).%s() );" +
			"}";
	
	private static String conditionOtherReturnFunc = "if( \"%s\".equals(prop) ) {" +
			"return (%s)((%s)obj).%s();" +				
			"}";
	
	private static String primitiveReturnFunc = "return %s.valueOf( ( (%s)obj).%s() );";	
	private static String otherReturnFunc = "return ((%s)obj).%s();";				
			
	
	
	
	public static Object INSTANCE(Class<?> classInstance) throws Exception
	{
		ClassPool pool = ClassPool.getDefault();
		String classSimpleName = classInstance.getName().replace('.', '_') + "_RefUtil";
		String fqClassName = packageName + classSimpleName;
		//System.out.println("Generating class ..." + fqClassName);
		CtClass cc = null;
		CtClass parentClass = pool.get(BaseProperty.class.getName());		
		
		try {
			cc = pool.get(classSimpleName);
		} catch (NotFoundException e) {
			cc = pool.makeClass(fqClassName);
			cc.setSuperclass(parentClass);
			StringBuilder methodBody = new StringBuilder();

			for (Method methodName : classInstance.getMethods()) {
				if (methodName.getName().startsWith("get")) {
					String returnType = methodName.getReturnType().getName();
					if (methodName.getReturnType().isPrimitive()) {
						returnType = PrimitiveType.valueOf(returnType+"_type").getObjectClass().getName();
						methodBody.append(String.format(conditionPrimitiveReturnFunc, methodName.getName(),returnType, classInstance.getName(), methodName.getName()));						
					} else {
						methodBody.append(String.format(conditionOtherReturnFunc, methodName.getName(),returnType, classInstance.getName(), methodName.getName()));						
					}
				}
			}			
			String setterSource = "public Object getValue(Object obj,String prop){ "+ methodBody.toString() + "return null;" + "}";		
			CtMethod method = CtNewMethod.make(setterSource, cc);
			cc.addMethod(method);
			cc.writeFile();
		}
		//System.out.println(fqClassName + " class generated");
		Class<?> classObject = cc.toClass(PropertyGenerator.class.getClassLoader(),PropertyGenerator.class.getProtectionDomain());
		return classObject.newInstance();
	}
	
	
	public static Object INSTANCE(Class<?> classInstance,String propertyName) throws Exception
	{
		ClassPool pool = ClassPool.getDefault();
		String classSimpleName = classInstance.getName().replace('.', '_') + propertyName + "_RefUtil";
		String fqClassName = packageName + classSimpleName;
		//System.out.println("Generating class ..." + fqClassName);
		CtClass cc = null;
		CtClass parentClass = pool.get(BaseProperty.class.getName());		
		
		try {
			cc = pool.get(classSimpleName);
		} 
		catch (NotFoundException e) 
		{
			cc = pool.makeClass(fqClassName);
			cc.setSuperclass(parentClass);
			StringBuilder methodBody = new StringBuilder();
			Method methodName = classInstance.getMethod(propertyName);
			String returnType = methodName.getReturnType().getName();
			if (methodName.getReturnType().isPrimitive()) {
				returnType = PrimitiveType.valueOf(returnType+"_type").getObjectClass().getName();
				methodBody.append(String.format(primitiveReturnFunc, returnType, classInstance.getName(), methodName.getName()));				
			} else {
				methodBody.append(String.format(otherReturnFunc, classInstance.getName(), methodName.getName()));						
			}
			
						
			String setterSource = "public Object getValue(Object obj){ "+ methodBody.toString() + "return null;" + "}";		
			CtMethod method = CtNewMethod.make(setterSource, cc);
			cc.addMethod(method);
			cc.writeFile();
		}
		//System.out.println(fqClassName + " class generated");
		Class<?> classObject = cc.toClass(PropertyGenerator.class.getClassLoader(),PropertyGenerator.class.getProtectionDomain());
		return classObject.newInstance();
	}
	
	public enum PrimitiveType
	{
		int_type{
			public Class<?> getObjectClass(){return Integer.class;}
		},
		float_type{
			public Class<?> getObjectClass(){return Float.class;}
		},
		long_type{
			public Class<?> getObjectClass(){return Long.class;}
		},
		double_type{
			public Class<?> getObjectClass(){return Double.class;}
		},
		char_type{
			public Class<?> getObjectClass(){return Character.class;}
		}
		,
		byte_type{
			public Class<?> getObjectClass(){return Byte.class;}
		};
		public Class<?> getObjectClass(){return null;};		
	}
	
	public static class BaseProperty {
		public Object getValue(Object o, String propName) {
			return null;
		}
		
		public Object getValue(Object o) {
			return null;
		}
	}	
}
