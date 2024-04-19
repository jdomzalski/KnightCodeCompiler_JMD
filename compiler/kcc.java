/** 

* This is the class that kicks off the compiler. 

* The class asks the user for an input and output file,  

* then calls and runs the compiler 

* @author Joshua Domzalski 

* @version 1.0 

* Assignment 5 

* CS322 - Compiler Construction 

* Spring 2024 

**/ 

package compiler; 

  
import org.antlr.v4.runtime.*; 

import org.antlr.v4.runtime.CharStreams; 

import org.antlr.v4.runtime.CommonTokenStream; 

import org.antlr.v4.runtime.tree.ParseTree; 

  

import org.objectweb.asm.*; 

  
import lexparse.KnightCodeLexer; 

import lexparse.KnightCodeParser; 

import lexparse.KnightCodeBaseVisitor; 

import compiler.MyVisitor; 

  

import java.io.*; 

import java.util.ArrayList; 

import java.util.Scanner; 


public class kcc { 

	public static void main(String[] args) throws Exception{ 

		 
		// Scanner to read input from user 

		Scanner scan = new Scanner(System.in); 

		 
		// ArrayList to store input and output files 

		ArrayList<String> files = new ArrayList(); 

		// Getting the path of the input file 

		if(files.size() == 0) { 

			System.out.println("Enter the path of the input file: "); 

		} 

		String inputFile = scan.nextLine(); 

		files.add(inputFile); 

		// Getting the output file 

		if(files.size() == 1) { 

			System.out.println("Enter the output file: "); 

		} 

		String outputFile = scan.nextLine(); 

		files.add(outputFile); 
	 

		// Generate a tree and feed visitor with that tree 

		// Need an ANTLR input stream (input will be input file name) 

		 
		CharStream input = CharStreams.fromFileName(inputFile); 

		//Creating the lexer 

		KnightCodeLexer lexer = new KnightCodeLexer(input); 

		// Creating the tokens from the lexer 

		CommonTokenStream tokens = new CommonTokenStream(lexer); 

		// Feed parser with tokens 

		KnightCodeParser parser = new KnightCodeParser(tokens); 

		// Can then use whole thing to generate a parse tree 

		ParseTree tree = parser.file(); // file is the entry rule for our grammar 

		// Create the visitor and visit the parse tree we generated 

		MyVisitor visitor = new MyVisitor(); 

		// Generate bytecode as the parse tree is visted 

		byte[] bytecode = visitor.visit(tree); 

		// Writing the output file 

		try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)){ 

			fileOutputStream.write(bytecode); 

		} 
	} // End main 

  

} // End class 

 
