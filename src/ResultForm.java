import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class ResultForm{
    private JLabel detailLabel;
    private JLabel featureLabel;
    private JLabel weightLabel;
    private JLabel likelinessLabel;
    private JPanel panel;

    public ResultForm(String featureName,float lowerRange,float upperRange,String weight,String likeness){
        featureLabel.setText(generateFeatureDescription(featureName,lowerRange,upperRange));
        detailLabel.setText(generateDetailDescription(lowerRange,upperRange));
        weightLabel.setText(weight);
        likelinessLabel.setText(likeness);
    }

    public ResultForm(String likeness){
        featureLabel.setText("Overall");
        detailLabel.setText("");
        weightLabel.setText(likenessToString(likeness));
        likelinessLabel.setText(likeness);
    }

    private String likenessToString(String likeness){
        float floatValue = Float.parseFloat(likeness) / 100;
        if(floatValue == 0.0f)
            return "No Likeness";
        if(floatValue > 0.0f && floatValue <= 0.2f)
            return "Negligible Likeness";
        if(floatValue > 0.2f && floatValue <= 0.4f)
            return "Very Low Likeness";
        if(floatValue > 0.4f && floatValue <= 0.6f)
            return "Low Likeness";
        if(floatValue > 0.6f && floatValue <= 0.8f)
            return "Moderate Likeness";
        if(floatValue > 0.8f && floatValue <= 0.95f)
            return "High Likeness";
        if(floatValue > 0.95f && floatValue < 1.0f)
            return "Very High Likeness";
        else
            return "Perfect Likeness";
    }

    private String generateFeatureDescription(String featureName,float lowerRange,float upperRange){
        String lowerString = rangeToString(lowerRange);
        String upperString = rangeToString(upperRange);
        String rangeDescription = "";
        if(lowerString.equals(upperString))
            rangeDescription = String.format("(%s elevations)",lowerString);
        else
            rangeDescription = String.format("(%s to %s elevations)",lowerString,upperString);
        return String.format("<html>%s<br/>%s<html>",featureName,rangeDescription);
    }

    private String rangeToString(float range){
        if(range == 0.0f)
            return "Lowest";
        if(range > 0.0f && range <= 0.4f)
            return "Lower";
        if(range > 0.4f && range <= 0.6f)
            return "Average";
        if(range > 0.6f && range < 1.0f)
            return "Higher";
        if(range == 1.0f)
            return "Highest";
        else
            return "Invalid";
    }

    private String generateDetailDescription(float lowerRange,float upperRange){
        return String.format("%." + AdvancedConfigurations.visual_accuracy + "f%% to %." + AdvancedConfigurations.visual_accuracy +
                "f%%",lowerRange * 100,upperRange * 100);
    }

    public JPanel getPanel(){
        return panel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$(){
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(1,4,new Insets(0,0,0,0),-1,-1,true,false));
        featureLabel = new JLabel();
        Font featureLabelFont = this.$$$getFont$$$(null,-1,14,featureLabel.getFont());
        if(featureLabelFont != null) featureLabel.setFont(featureLabelFont);
        featureLabel.setText("Label");
        panel.add(featureLabel,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_WANT_GROW,null,new Dimension(146,20),null,0,false));
        detailLabel = new JLabel();
        Font detailLabelFont = this.$$$getFont$$$(null,-1,14,detailLabel.getFont());
        if(detailLabelFont != null) detailLabel.setFont(detailLabelFont);
        detailLabel.setText("Label");
        panel.add(detailLabel,new GridConstraints(0,1,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        weightLabel = new JLabel();
        Font weightLabelFont = this.$$$getFont$$$(null,-1,14,weightLabel.getFont());
        if(weightLabelFont != null) weightLabel.setFont(weightLabelFont);
        weightLabel.setText("Label");
        panel.add(weightLabel,new GridConstraints(0,2,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        likelinessLabel = new JLabel();
        Font likelinessLabelFont = this.$$$getFont$$$(null,-1,14,likelinessLabel.getFont());
        if(likelinessLabelFont != null) likelinessLabel.setFont(likelinessLabelFont);
        likelinessLabel.setText("Label");
        panel.add(likelinessLabel,new GridConstraints(0,3,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName,int style,int size,Font currentFont){
        if(currentFont == null) return null;
        String resultName;
        if(fontName == null){
            resultName = currentFont.getName();
        }
        else{
            Font testFont = new Font(fontName,Font.PLAIN,10);
            if(testFont.canDisplay('a') && testFont.canDisplay('1')){
                resultName = fontName;
            }
            else{
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName,style >= 0 ? style : currentFont.getStyle(),size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name","").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(),font.getStyle(),font.getSize()) : new StyleContext().getFont(font.getFamily(),font.getStyle(),font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$(){
        return panel;
    }

}
