/*
Tom Beach
Copyright (c) Cardiff University
*/

package eu.accordproject.buildingcodesandrules;

import org.dcom.core.compliancedocument.ComplianceDocument;
import org.dcom.core.compliancedocument.ComplianceItem;
import org.dcom.core.compliancedocument.Section;
import org.dcom.core.compliancedocument.Paragraph;
import org.dcom.core.compliancedocument.Table;
import org.dcom.core.compliancedocument.Figure;
import java.util.List;
import org.dcom.core.compliancedocument.inline.*;
import java.util.ArrayList;

public class Filters {

	public static ComplianceDocument executionFilter(ComplianceDocument document) {
		System.out.println("Performing Execution Filter");
		 if (document.getNoSections() > 0) {
          List<ComplianceItem> subItems = document.getSubItems();
          for (int i=0; i < subItems.size();i++) {
          	ComplianceItem subItem = subItems.get(i);
          	boolean execution = checkSection((Section)subItem);
          	if (!execution) {
          		document.removeSubItem((Section)subItem);
          		i--;
          	}
          }
        }
        return document;
	}

	private static boolean checkSection(Section s) {
		if (s.getNoSubItems() > 0) {
        	List<ComplianceItem> subItems = s.getSubItems();
          	for (int i=0; i < subItems.size();i++) {
          		ComplianceItem subItem = subItems.get(i);
            	if (subItem instanceof Section ) {
            		boolean execution = checkSection((Section)subItem);
            		if (!execution) {
            			s.removeSubItem(subItem);
            			i--;
            		}
            	} else if (subItem instanceof Paragraph ) {
            		boolean execution = checkParagraph((Paragraph)subItem);
            		if (!execution) {
            			s.removeSubItem(subItem);
            			i--;
            		}
            	}
          	}
        }

        if (s.getNoSubItems() > 0) return true;
        return false;
       
	}

	private static boolean checkParagraph(Paragraph p) {
 		if (p.getNoSubItems()> 0) {
        	List<ComplianceItem> subItems = p.getSubItems();
          	for (int i=0; i < subItems.size();i++) {
          		ComplianceItem subItem = subItems.get(i);
            	if (subItem instanceof Table || subItem instanceof Figure) {
            		p.removeSubItem(subItem);
            		i--;
            	}
            	else if (subItem instanceof Paragraph) {
            		boolean execution = checkParagraph((Paragraph)subItem);
            		if (!execution) {
            			p.removeSubItem(subItem);
            			i--;
            		}
            	}
         	}
      	}
      	 //now check the inline items

        List<InlineItem> inlineItems = p.getInlineItems();
        for (int k=0; k < inlineItems.size();k++) {
           	boolean execution = checkInlineItem(inlineItems.get(k));
            if (!execution) {
            	inlineItems.remove(inlineItems.get(k));
        		k--;
        	}
        }
   
        if (p.getNoSubItems() > 0 || p.getInlineItems().size() > 0) return true;
        return false;
	}

	public static boolean checkInlineItem(InlineItem i) {
		if (i instanceof InlineString) return false;
		if (i instanceof RASEBox) {
			 RASEBox box = ((RASEBox)i);
			 List<InlineItem> toRemove = new ArrayList<InlineItem>();
			 for (int x=0; x < box.getNoSubItems(); x++) {
			 	boolean execution = checkInlineItem(box.getSubItem(x));
				if (!execution) toRemove.add(box.getSubItem(x));
			 }
			for (InlineItem item : toRemove) box.removeSubItem(item);
			if (box.getNoSubItems() ==0) return false;
			else return true;
		}
		if (i instanceof RASETag && ((RASETag)i).getType() != 0)  return true;
		return false;
	}
}