package compiler;

// Imports
import java.util.HashMap;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import lexparse.KnightCodeBaseVisitor;
import lexparse.KnightCodeParser;
import lexparse.KnightCodeParser.AdditionContext;

// Class type needs to be byte so we can have methods where bytecode operations occur
public class MyVisitor extends KnightCodeBaseVisitor<byte[]>{
	
	// Creating a HashMap to store the variables visited
	HashMap<String, Object> variables = new HashMap<>();
	// Creating the ClassWriter
	public ClassWriter classWriter;
	// Creating the MethodVisitor
	public MethodVisitor methodVisitor;
	
	/*
	 * Main constructor method
	 */
	public MyVisitor() {
		// The Class Writer can be used to generate bytecode for a class
		classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		
		// Standard visit and setup process of the class
		classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "", null, "java/lang/Object", null);
		
		// The Method Visitor visiting the main method
		methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		methodVisitor.visitCode();		
	}
	
	/*
	 * Visiting the entry method that visits the contents of a file
	 */
	@Override
	public byte[] visitFile(KnightCodeParser.FileContext ctx) {
		// Visit the Declare
		visitDeclare(ctx.declare());
		// Visit Body
		visitBody(ctx.body());
		
		// Returning the main method
		methodVisitor.visitInsn(Opcodes.RET);
		
		// Sets the maximum sizes of the stack/local variables
		methodVisitor.visitMaxs(0, 0);
		
		// Ending method
		methodVisitor.visitEnd();
		
		// Get the bytecode generated
		classWriter.visitEnd();
		return classWriter.toByteArray();
		
	}
	
	/*
	 * This method checks to see if a variable is a string or an integer
	 * and places/initializes the variable into the HashMap
	 */
	@Override
	public byte[] visitDeclare(KnightCodeParser.DeclareContext ctx) {
		// Going through each variable that was declared
		for(KnightCodeParser.VariableContext variableContext : ctx.variable()) {
			// Getting the name and type of each variable
			String name = variableContext.identifier().getText();
			String type = variableContext.vartype().getText();
			// Checking to see if a variable is an integer
			if(type.equals("INTEGER")) {
				// Adding variable to intVariables HashMap and initializing the variable 
				variables.put(name, 0);
			}
			if(type.equals("STRING")) {
				// Adding variable to stringVariables HashMap and initializing the variable
				variables.put(name, "");
			}
			
		}
		// Returning
		return null;
	}
	
	/*
	 * The method to set the value of the variables
	 */
	@Override
	public byte[] visitSetvar(KnightCodeParser.SetvarContext ctx) {
		// Getting the name of the variable that will be assigned
		String name = ctx.ID().getText();
		// Getting the variable's index from the HashMap.
		int index = (int) variables.get(name);
		
		// Visiting the expression and getting the assignment value
		// Generating bytecode needed to compute the value
		visit(ctx.expr());
	
		// Using bytecode to store value into the variable
		methodVisitor.visitVarInsn(Opcodes.ISTORE, index);
		
		// Returning
		return null;
	}
	
	/*
	 * This is the method that prints strings and integers to the screen.
	 * The method checks if a variable is an integer or not before printing
	 * If a variable is not an integer, it is assumed to be a string
	 */
	@Override
	public byte[] visitPrint(KnightCodeParser.PrintContext ctx) {
		// Check if we are just printing a string
		if(ctx.STRING() != null) {
			
			String string = ctx.STRING().getText().replaceAll("\"", "");
			System.out.println(string);
		}
		
		else if (ctx.ID() != null) {
			String variableName = ctx.ID().getText();
			
			if(variables.get(variableName) instanceof Integer) {
				System.out.println((int) variables.get(variableName));
			} else {
				System.out.println(variables.get(variableName));
			}
		}
		
		return null;
	}
	
	/*
	 * Performing addition using bytecode
	 */
	@Override
	public byte[] visitAddition(KnightCodeParser.AdditionContext ctx) {
		// Getting the value of the first expression from the stack
		visit(ctx.expr(0));
		// Getting the value of the second expression from the stack
		visit(ctx.expr(1));
		
		// Performing the integer addition in bytecode
		methodVisitor.visitInsn(Opcodes.IADD);
		
		// Returning
		return null;
	}
	
	/*
	 * Performing subtraction using bytecode
	 */
	@Override
	public byte[] visitSubtraction(KnightCodeParser.SubtractionContext ctx) {
		// Getting the value of the first expression from the stack
		visit(ctx.expr(0));
		// Getting the value of the second expression from the stack
		visit(ctx.expr(1));
		
		// Performing the integer subtraction in bytecode
		methodVisitor.visitInsn(Opcodes.ISUB);
		
		// Returning
		return null;
	}
	
	/*
	 * Performing multiplication using bytecode
	 */
	@Override
	public byte[] visitMultiplication(KnightCodeParser.MultiplicationContext ctx) {
		// Getting the value of the first expression from the stack
		visit(ctx.expr(0));
		// Getting the value of the second expression from the stack
		visit(ctx.expr(1));
		
		// Performing the integer multiplication in bytecode
		methodVisitor.visitInsn(Opcodes.IMUL);
		
		// Returning
		return null;
	}
	
	/*
	 * Performing division using bytecode
	 */
	@Override
	public byte[] visitDivision(KnightCodeParser.DivisionContext ctx) {
		// Getting the value of the first expression from the stack
		visit(ctx.expr(0));
		// Getting the value of the second expression from the stack
		visit(ctx.expr(1));
		
		// Performing the integer division in bytecode
		methodVisitor.visitInsn(Opcodes.IDIV);
		
		// Returning
		return null;
	}
	
	/*
	 * This is the method that will compare integer values.
	 * The comparisons are made using bytecode
	 */
	@Override
	public byte[] visitComparison(KnightCodeParser.ComparisonContext ctx) {
		// Getting the value of the first expression from the stack
		visit(ctx.expr(0));
		// Getting the value of the second expression from the stack
		visit(ctx.expr(1));
		
		// Since we are doing all computations in bytecode, I will also be
		// generating bytecode to make the comparisons between integers
		
		// A label that will make jumps depending on which logic is matched
		Label jump = new Label();
		// Getting the comparison operator
		String operator = ctx.comp().getChild(0).getText();
		
		// Adding a switch state to handle the different possible comparison operators
		switch(operator) {
		case ">":
			// If the first value is greater than the second value, make the jump
			methodVisitor.visitJumpInsn(Opcodes.IF_ICMPGT, jump);
			// Breaking out of the switch statement
			break;
		case "<":
			// If the first value is less than the second value, make the jump
			methodVisitor.visitJumpInsn(Opcodes.IF_ICMPLT, jump);
			break;
		case ">=":
			// If the first value is greater than or equal to the second value, make the jump
			methodVisitor.visitJumpInsn(Opcodes.IF_ICMPGE, jump);
			break;
		case "<=":
			// If the first value is less than or equal to the second value, make the jump
			methodVisitor.visitJumpInsn(Opcodes.IF_ICMPLE, jump);
			break;
		case "=":
			// If the first value is equal to the second value, make the jump
			methodVisitor.visitJumpInsn(Opcodes.IF_ICMPEQ, jump);
			break;
		case "<>":
			// If the first value is not equal to the second value, make the jump
			methodVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, jump);
			break;
		}
		
		// Using bytecode to push a 0 or 1 onto the stack
		// A 0 will be pushed for false, and a 1 will be pushed for true
		
		// Pushing a 0 on to the stack if the comparison returned a false value
		methodVisitor.visitInsn(Opcodes.ICONST_0);
		
		// Creating a label to signal the end of the comparison
		Label end = new Label();
		
		// Pushing a 1 on to the stack if the comparison returned a true value
		methodVisitor.visitLabel(jump);
		methodVisitor.visitInsn(Opcodes.ICONST_1);
		
		// Ending
		methodVisitor.visitLabel(end);
		
		// Returning
		return null;
		
	}
	
	
}
