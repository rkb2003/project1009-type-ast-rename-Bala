package visitor.rewrite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import model.ProgramElement;
import util.MsgUtil;

public class ReplaceClassVisitor extends ASTVisitor {
   private ProgramElement curProgElem;
   private String newClassName;
   private ICompilationUnit iUnit;
   private ASTRewrite rewrite;
   private CompilationUnit cUnit;

   public ReplaceClassVisitor(ProgramElement curProgElem, String newClassName) {
      this.curProgElem = curProgElem;
      this.newClassName = newClassName;
      //this.
   }

   @Override
   public boolean visit(TypeDeclaration node) {
      if (node.getName().getIdentifier().equals(curProgElem.getClassName()) == false) {
         return true;
      }

      // Description of the change
      SimpleName oldName = node.getName();
      SimpleName newName = cUnit.getAST().newSimpleName(newClassName);
      
      //TypeDeclaration newNode = cUnit.getAST().newTypeDeclaration();

      checkModifier(node, oldName);
      replaceType(node, oldName);
      rewrite.replace(oldName, newName, null);
      return super.visit(node);
   }
   
   /*private boolean isPublicModifier(TypeDeclaration node) {
	   List<Modifier> modList = node.modifiers();
	   
	   
   }*/

   private void checkModifier(TypeDeclaration node, SimpleName oldName) {
      List<?> modifiers = node.modifiers();
      System.out.println("[DBG] modifiers' size: " + modifiers.size());
      for (Object m : modifiers) {
         if (m instanceof Modifier) {
            Modifier mod = (Modifier) m;
            //checktype(node, mod);
            String modStr = mod.getKeyword().toString();
            
            if("public".equals (modStr) || "final".equals(modStr)) {
            	 MsgUtil.openQuestion("Do You Want to Rename this class ?");
           	  boolean val = true;
           	  if(!val) {
           		return;  
           	  }
           	              	
            	
            }
            System.out.println("[DBG] " + oldName + "'s Modifier: " + modStr);
         }
      }
   }

   private void replaceType(TypeDeclaration node,SimpleName oldName) {
      try {
    	  System.out.println("[DBG] "+ " Inside replaceType"+ node.getModifiers());
         // Update type java element accordingly
    	  
    	  if (node.equals("public") || "final".equals(node)) {
    	  boolean val = MsgUtil.openQuestion("Do You Want to Rename this class ?");
    	  if(!val) {
    		  MsgUtil.openWarning("You chose not to update the name. Thanks!");
    		return;  
    	  }
    	  }
    	  
         String iCUnitName = iUnit.getElementName();
         // A class, which is a nested class or exists below a class, throws JavaModelException.
         //if(iCUnitName.equals(oldName.getFullyQualifiedName())) 
         if (iCUnitName.replace(".java", "").equals(oldName.getFullyQualifiedName())) {
        	 MsgUtil.openWarning("The name"+ iCUnitName.replace(".java", "") +" is already taken. Please take a different name");
            IType oldType = iUnit.getType(oldName.getFullyQualifiedName());
            oldType.rename(newClassName, true, null);
         }
      } catch (JavaModelException e) {
         e.printStackTrace();
      }
   }

   public void setICompilationUnit(ICompilationUnit iUnit) {
      this.iUnit = iUnit;
   }

   public void setASTRewrite(ASTRewrite rewrite) {
      this.rewrite = rewrite;
   }

   public void setCompilationUnit(CompilationUnit cUnit) {
      this.cUnit = cUnit;
   }
}
