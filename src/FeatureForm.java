/*
TODO: Update ComparisonData after updating the components in the form
*/
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class FeatureForm{
    private JButton testButton;
    private JComboBox weightComboBox;
    private JPanel panel;
    private JTextField lowerRangeField;
    private JTextField upperRangeField;
    private ComparisonData data = new ComparisonData("Temporary");

    HashMap<String,String> featureDescriptions = new HashMap<String,String>();

    public FeatureForm(int featureId,ComparisonData data,MainApplication window){
        this(featureId,data.getFeatureName(),data.getLowerRange() + "",data.getUpperRange() + "",data.getWeight(),
                window);
        this.data = data;
    }

    public FeatureForm(int featureId,String featureName,String lowerRangeValue,String upperRangeValue,
                       int range,MainApplication window){
        this(featureId,featureName,window);
        lowerRangeField.setText(lowerRangeValue);
        upperRangeField.setText(upperRangeValue);
        weightComboBox.setSelectedIndex(range - 1);
    }

    public FeatureForm(int featureId,String featureName,MainApplication window){
        // populate featureDescriptions
        featureDescriptions.put("Level","This algorithm returns the percentage of the terrain data that is within the specified range of terrain levels");
        featureDescriptions.put("Exclude Level","This algorithm returns the percentage of the terrain data that is " +
                "outside the specified range of terrain levels");
        featureDescriptions.put("Max Horizontal","This algorithm returns the longest horizontal single-line specified range of terrain levels");
        featureDescriptions.put("Max Vertical","This algorithm returns the longest vertical single-line specified range of terrain levels");
        featureDescriptions.put("Max Diagonal","This algorithm returns the longest 45 degree single-line of specified range of terrain levels");
        featureDescriptions.put("Max Area","This algorithm returns the percentage of the area (in pixels) of the largest bounded area of specified range of terrain");

        testButton.setText(featureName);
        testButton.setToolTipText(featureDescriptions.get(featureName));
        // TODO: Change setVisible to something else as it doesn't really remove the component
        testButton.addActionListener(e -> {
            panel.setVisible(false);
            window.removeFeature(featureId);
        });
        testButton.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent evt){
                testButton.setBackground(new Color(220,20,60));
            }

            public void mouseExited(MouseEvent evt){
                testButton.setBackground(UIManager.getColor("control"));
            }
        });

        lowerRangeField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e){
                String text = lowerRangeField.getText();
                try{
                    // To invoke catch
                    float input = Float.parseFloat(text);

                    if(input < 0){
                        lowerRangeField.setText("0.0");
                        data.setLowerRange(0);
                    }
                    else if(input > 1){
                        lowerRangeField.setText("1.0");
                        data.setLowerRange(1);
                    }
                    else
                        data.setLowerRange(Float.parseFloat(text));
                }catch(Exception f){
//                    System.out.println("String is input instead of float");
                    data.setLowerRange(-1);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e){
            }

            @Override
            public void changedUpdate(DocumentEvent e){
            }
        });

        upperRangeField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e){
                String text = upperRangeField.getText();
                try{
                    // To invoke catch
                    float input = Float.parseFloat(text);

                    if(input < 0){
                        upperRangeField.setText("0.0");
                        data.setUpperRange(0);
                    }
                    else if(input > 1){
                        upperRangeField.setText("1.0");
                        data.setUpperRange(1);
                    }
                    else
                        data.setUpperRange(Float.parseFloat(text));
                }catch(Exception f){
//                    System.out.println("String is input instead of float");
                    data.setUpperRange(-1);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e){

            }

            @Override
            public void changedUpdate(DocumentEvent e){

            }
        });

        weightComboBox.addActionListener(e -> {
            try{
                data.setWeight(weightComboBox.getSelectedIndex() + 1);
            }catch(NullPointerException f){
//                System.out.println("WeightComboBox is null");
            }
        });
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
        panel.setLayout(new GridLayoutManager(1,2,new Insets(0,0,0,0),-1,-1));
        testButton = new JButton();
        testButton.setText("Max Horizontal");
        panel.add(testButton,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_FIXED,null,new Dimension(150,-1),new Dimension(150,-1),0,false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1,5,new Insets(0,0,0,0),-1,-1));
        panel.add(panel1,new GridConstraints(0,1,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        final JLabel label1 = new JLabel();
        label1.setText("to");
        panel1.add(label1,new GridConstraints(0,1,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        lowerRangeField = new JTextField();
        panel1.add(lowerRangeField,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_WANT_GROW,GridConstraints.SIZEPOLICY_FIXED,null,new Dimension(60,-1),new Dimension(60,-1),0,false));
        upperRangeField = new JTextField();
        panel1.add(upperRangeField,new GridConstraints(0,2,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_WANT_GROW,GridConstraints.SIZEPOLICY_FIXED,null,new Dimension(60,-1),new Dimension(60,-1),0,false));
        weightComboBox = new JComboBox();
        weightComboBox.setMaximumRowCount(10);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("1");
        defaultComboBoxModel1.addElement("2");
        defaultComboBoxModel1.addElement("3");
        defaultComboBoxModel1.addElement("4");
        defaultComboBoxModel1.addElement("5");
        defaultComboBoxModel1.addElement("6");
        defaultComboBoxModel1.addElement("7");
        defaultComboBoxModel1.addElement("8");
        defaultComboBoxModel1.addElement("9");
        defaultComboBoxModel1.addElement("10");
        weightComboBox.setModel(defaultComboBoxModel1);
        panel1.add(weightComboBox,new GridConstraints(0,4,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_FIXED,null,new Dimension(50,-1),new Dimension(50,-1),0,false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1,new GridConstraints(0,3,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_WANT_GROW,1,null,null,null,0,false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$(){
        return panel;
    }

}
