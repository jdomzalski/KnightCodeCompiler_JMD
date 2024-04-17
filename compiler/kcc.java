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
import java.util.Scanner;

public class kcc {

	public static void main(String[] args) throws Exception{
		
		// Generate a tree and feed visitor with that tree
		// Need an ANTLR input stream (input will be input file name)
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Input path of input file: ");
		String inputFile = scan.nextLine();
		
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

	}

}
