package analysis;
/*
 * @(#) ASTAnalyzer.java
 *
 * Copyright 2015-2018 The Software Analysis Laboratory
 * Computer Science, The University of Nebraska at Omaha
 * 6001 Dodge Street, Omaha, NE 68182.
 */

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import model.ProgramElement;
import util.MsgUtil;
import visitor.rewrite.ReplaceMethodVisitor;

public class ReplaceMethodNameAnalyzer1 extends ProjectAnalyzer {
   private ProgramElement curProgElem;
   private String newMethodName;
   boolean matchedExistingMethod = false;

   public ReplaceMethodNameAnalyzer1(ProgramElement curProgName, String newMethodName) {
      this.curProgElem = curProgName;
      this.newMethodName = newMethodName;
   }

   @Override
   protected void analyzeCompilationUnit(ICompilationUnit[] iCompilationUnits) throws JavaModelException {
      try {
         replaceMethodName(iCompilationUnits);
      } catch (MalformedTreeException | BadLocationException e) {
         e.printStackTrace();
      }
   }

  /* void replaceMethodName(ICompilationUnit[] iCompilationUnits) throws JavaModelException, MalformedTreeException, BadLocationException {
      for (ICompilationUnit iCUnit : iCompilationUnits) {
         ICompilationUnit workingCopy = iCUnit.getWorkingCopy(null); // create DOM/AST from a ICompilationUnit
         CompilationUnit cUnit = parse(workingCopy); // create AST
         ASTRewrite rewrite = ASTRewrite.create(cUnit.getAST()); // create ASTRewrite

         ReplaceMethodVisitor visitor = new ReplaceMethodVisitor(curProgElem, newMethodName);
         visitor.setAST(cUnit.getAST());
         visitor.setASTRewrite(rewrite);
         cUnit.accept(visitor);

         applyTextEditAndCommit(workingCopy, rewrite);
      }
   }*/
   
   
   void replaceMethodName(ICompilationUnit[] iCompilationUnits) throws JavaModelException, MalformedTreeException, BadLocationException {
	      if (this.pkgName.equals(curProgElem.getPkgName()) == false) {
	         return;
	      }

	      for (ICompilationUnit iCUnit : iCompilationUnits) {
	         if (iCUnit.getElementName().replace(".java", "").equals(curProgElem.getClassName()) == false) {
	            continue;
	         }
	         CompilationUnit cUnit = parse(iCUnit);
	         cUnit.accept(new ASTVisitor() {
	            @Override
	            public boolean visit(MethodDeclaration node) {
	               // TODO - Class Exercise - complete this visitor.

	               return true;
	            }
	         });
	      }

	      
		if (matchedExistingMethod) {
	         System.out.println("[WRN] Cannot overwrite an existing method");
	         MsgUtil.openWarning("The name " + newMethodName + " is already taken. " //
	               + "Please choose a different name");
	         return;
	      }

	      for (ICompilationUnit iCUnit : iCompilationUnits) {
	         ICompilationUnit workingCopy = iCUnit.getWorkingCopy(null);
	         CompilationUnit cUnit = parse(workingCopy);
	         ASTRewrite rewrite = ASTRewrite.create(cUnit.getAST());
	         ReplaceMethodVisitor visitor = new ReplaceMethodVisitor(curProgElem, newMethodName);
	         visitor.setAST(cUnit.getAST());
	         visitor.setASTRewrite(rewrite);
	         cUnit.accept(visitor);
	         TextEdit edits = rewrite.rewriteAST(); // Compute the edits
	         workingCopy.applyTextEdit(edits, null); // Apply the edits.
	         workingCopy.commitWorkingCopy(false, null); // Save the changes.
	      }
	   }



   // void addParameter(MethodDeclaration decl, ICompilationUnit workingCopy) throws JavaModelException, IllegalArgumentException {
   // AST ast = decl.getAST();
   // ASTRewrite astRewrite = ASTRewrite.create(ast);
   //
   // SimpleName newName = ast.newSimpleName("newName");
   // astRewrite.set(decl, MethodDeclaration.NAME_PROPERTY, newName, null);
   //
   // ListRewrite paramRewrite = astRewrite.getListRewrite(decl, MethodDeclaration.PARAMETERS_PROPERTY);
   //
   // SingleVariableDeclaration newParam = ast.newSingleVariableDeclaration();
   // newParam.setType(ast.newPrimitiveType(PrimitiveType.INT));
   // newParam.setName(ast.newSimpleName("p1"));
   // paramRewrite.insertFirst(newParam, null);
   //
   // TextEdit edit = astRewrite.rewriteAST();
   // workingCopy.applyTextEdit(edit, null);
   // workingCopy.commitWorkingCopy(false, null);
   // }

}