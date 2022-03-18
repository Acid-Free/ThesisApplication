/*
Observations:
Level and Exclude Level are fundamentally the same

Recommendations:
Show the users actual terrain feature values instead of just comparison values

TODO: force users to input two terrain files and input at least one terrain feature before proceeding
TODO: add a default terrain feature filled with range and weight
TODO: enforce a max terrain feature (14) as it overflows otherwise
*/
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainApplication extends JFrame{
    private JPanel cardPanel;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JButton proceed1;
    private JButton proceed2;
    private JButton selectTerrainData1Button;
    private JButton selectTerrainData2Button;
    private JButton return2;
    private JPanel main3Panel;
    private JButton return3;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel terrainImageLabel1;
    private JLabel terrainImageLabel2;
    private JLabel resultsTerrain1;
    private JLabel resultsTerrain2;
    private JPanel panel0;
    private JButton helpButton;
    private JLabel terrain1Width;
    private JLabel terrain1Height;
    private JLabel terrain1Highest;
    private JLabel terrain1Average;
    private JLabel terrain1Lowest;
    private JLabel terrain2Width;
    private JLabel terrain2Height;
    private JLabel terrain2Highest;
    private JLabel terrain2Average;
    private JLabel terrain2Lowest;
    private JLabel terrain1Name;
    private JLabel terrain2Name;
    private JPanel panelTerrainFeaturesMain;
    private JPanel comparisonPanel;
    private JButton addFeatureButton;
    private JComboBox featureComboBox;
    private JButton proceed0;
    private JPanel helpPanel;
    private JPanel resultsPanel;
    private JPanel overallPanel;
    private JLabel resultsTerrain1Name;
    private JLabel resultsTerrain2Name;
    private JLabel helpLabel;
    private JScrollPane scrollPane;

    HashMap<Integer,ComparisonData> allComparisonData = new HashMap<>();
    int featureId;

    public static void main(String[] args) throws IOException{

        // Custom UI theme
//        try{
//            UIManager.setLookAndFeel(new DarculaLaf());
//        }catch(UnsupportedLookAndFeelException e){
//            e.printStackTrace();
//        }

        TerrainData terrain1 = new TerrainData();
        TerrainData terrain2 = new TerrainData();

        TerrainImageHelper imageHelper = new TerrainImageHelper();
        TerrainComparisonHelper comparisonHelper = new TerrainComparisonHelper();

        MainApplication window = new MainApplication();
        CardLayout c1 = (CardLayout)window.cardPanel.getLayout();

        window.proceed0.addActionListener(e -> c1.next(window.getContentPane()));
        // TODO: Ensure that the user had selected the two terrain files before proceeding; otherwise, notify them
        window.proceed1.addActionListener(e -> {
            c1.next(window.getContentPane());
            window.initializeIcons(terrain1,terrain2,imageHelper);
            try{
                imageHelper.computeTerrainLevels(terrain1);
                imageHelper.computeTerrainLevels(terrain2);
            }catch(NullPointerException f){
                System.out.println("Incomplete terrain data, stopped computing terrain levels");
            }
            window.updateMainWindow(terrain1,terrain2);
        });

        window.proceed2.addActionListener(e -> {
            c1.next(window.getContentPane());
            window.updateResultsWindow(comparisonHelper,terrain1,terrain2);
        });

        window.helpButton.addActionListener(e -> c1.previous(window.getContentPane()));
        window.return2.addActionListener(e -> c1.previous(window.getContentPane()));
        window.return3.addActionListener(e -> c1.previous(window.getContentPane()));

        window.selectTerrainData1Button.addActionListener(e -> {
            // Approach A
//            if(window.createTerrainFileChooser(terrain1,imageHelper))
//                window.selectTerrainData1Button.setText(terrain1.getTerrainName());
            // Approach B
            if(window.createTerrainFileChooserNative(terrain1,imageHelper))
                window.selectTerrainData1Button.setText(terrain1.getTerrainName());
        });
        window.selectTerrainData2Button.addActionListener(e -> {
            // Approach A
//            if(window.createTerrainFileChooser(terrain2,imageHelper))
//                window.selectTerrainData2Button.setText(terrain2.getTerrainName());
            // Approach B
            if(window.createTerrainFileChooserNative(terrain2,imageHelper))
                window.selectTerrainData2Button.setText(terrain2.getTerrainName());
        });

        // Initialize help section
        ImageIcon helpIcon = new ImageIcon(window.getClass().getResource("help_image.PNG"));
        window.helpLabel.setIcon(helpIcon);

        window.addFeatureButton.addActionListener(e -> {
            window.addSelectedFeature();

            System.out.println(window.featureComboBox.getSelectedIndex());
            window.comparisonPanel.revalidate();
        });

        // default terrain feature
        ++window.featureId;
        ComparisonData defaultComparisonData = new ComparisonData("Level",0.0f,1.0f,1);
        window.allComparisonData.put(window.featureId,defaultComparisonData);
        window.comparisonPanel.add(new FeatureForm(window.featureId,defaultComparisonData,window).getPanel());

        // General application window configurations
        window.setContentPane(window.cardPanel);
        window.setResizable(false);
        window.setTitle("CompTerra 0.1.0");
        window.setSize(1280,720);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        c1.next(window.getContentPane());
    }

    // TODO: Currently placeholder, update after completing the algorithms for terrain features and comparison
    void updateResultsWindow(TerrainComparisonHelper comparisonHelper,TerrainData terrain1,TerrainData terrain2){

        resultsTerrain1Name.setText(terrain1.getTerrainName());
        resultsTerrain2Name.setText(terrain2.getTerrainName());

        // TODO: Try to find a more elegant way to fix this (unnecessary reload)
        this.resultsPanel.removeAll();
        this.overallPanel.removeAll();

        float overallSimilarity = 0.0f;
        int totalWeight = 0;

        // Computes the comparison value and similarity of the terrain data
        for(Map.Entry m: allComparisonData.entrySet()){
            // m.getKey() and m.getValue() mainly used inside this loop
            // TODO: fix this suspicious call (double downcast?)
            ComparisonData currentComparison = allComparisonData.get(m.getKey());

//            String featureName = currentComparison.getFeatureName();
//            String detail = String.format("%.2f to %.2f",currentComparison.getLowerRange(),currentComparison.getUpperRange());
//            String weight = String.format("%d",currentComparison.getWeight());
//            String likeliness = "94% (placeholder)";
//            this.resultsPanel.add(new ResultForm(featureName,detail,weight,likeliness).getPanel());

            comparisonHelper.computeFeature(currentComparison,terrain1,terrain2);

            String featureName = currentComparison.getFeatureName();
            String weight = currentComparison.getWeight() + "";
            String likeliness =
                    String.format("%." + AdvancedConfigurations.accuracy + "f",currentComparison.getLikeliness());

            // Compute overall similarity
            totalWeight += currentComparison.getWeight();
            overallSimilarity += currentComparison.getLikeliness() * currentComparison.getWeight();


            this.resultsPanel.add(new ResultForm(featureName,currentComparison.getLowerRange(),currentComparison.getUpperRange(),
                    weight,likeliness).getPanel());
        }

        overallSimilarity /= totalWeight;
        this.overallPanel.add(new ResultForm(String.format("%." + AdvancedConfigurations.accuracy + "f",overallSimilarity)).getPanel());
    }

    void addSelectedFeature(){
        String featureName = this.featureComboBox.getSelectedItem().toString();
        ++featureId;
        ComparisonData newFormData = new ComparisonData(featureName,0.0f,1.0f,1);
        allComparisonData.put(featureId,newFormData);
        this.comparisonPanel.add(new FeatureForm(featureId,newFormData,this).getPanel());
    }

    void removeFeature(int featureId){
        allComparisonData.remove(featureId);
    }

    void updateMainWindow(TerrainData terrain1,TerrainData terrain2){
        this.terrain1Name.setText(terrain1.getTerrainName());
        this.terrain2Name.setText(terrain2.getTerrainName());
        this.terrain1Width.setText(Integer.toString(terrain1.getWidth()));
        this.terrain1Height.setText(Integer.toString(terrain1.getHeight()));
        this.terrain2Width.setText(Integer.toString(terrain2.getWidth()));
        this.terrain2Height.setText(Integer.toString(terrain2.getHeight()));
        this.terrain1Highest.setText(getPercentage(terrain1.getHighestLevel()));
        this.terrain1Average.setText(getPercentage(terrain1.getAverageLevel()));
        this.terrain1Lowest.setText(getPercentage(terrain1.getLowestLevel()));
        this.terrain2Highest.setText(getPercentage(terrain2.getHighestLevel()));
        this.terrain2Average.setText(getPercentage(terrain2.getAverageLevel()));
        this.terrain2Lowest.setText(getPercentage(terrain2.getLowestLevel()));
    }

    String getPercentage(float input){
        return String.format("%." + AdvancedConfigurations.accuracy + "f%%",input * 100);
    }

    void initializeIcons(TerrainData terrain1,TerrainData terrain2,
                         TerrainImageHelper imageHelper){
        // 385 is the maximum width of the JPanel that holds the JLabel for the icon (res 1280 x 720) before expanding
        ImageIcon terrainIcon1 = imageHelper.getTerrainImage(terrain1.getTerrainPath(),370,370);
        ImageIcon terrainIcon2 = imageHelper.getTerrainImage(terrain2.getTerrainPath(),370,370);
        ImageIcon terrainIcon3 = imageHelper.getTerrainImage(terrain1.getTerrainPath(),250,250);
        ImageIcon terrainIcon4 = imageHelper.getTerrainImage(terrain2.getTerrainPath(),250,250);

        this.terrainImageLabel1.setIcon(terrainIcon1);
        this.terrainImageLabel2.setIcon(terrainIcon2);
        this.resultsTerrain1.setIcon(terrainIcon3);
        this.resultsTerrain2.setIcon(terrainIcon4);

    }

    boolean createTerrainFileChooser(TerrainData terrainData,TerrainImageHelper imageHelper){
        JFileChooser fileChooser = new JFileChooser();

        int response = fileChooser.showOpenDialog(null);

        if(response == JFileChooser.APPROVE_OPTION){
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            terrainData.setTerrainName(fileChooser.getSelectedFile().getName());
            loadTerrainData(file.toString(),terrainData,imageHelper);
            return true;
        }
        return false;
    }

    boolean createTerrainFileChooserNative(TerrainData terrainData,TerrainImageHelper imageHelper){
        FileDialog fileDialog = new FileDialog(this,"Select terrain file",FileDialog.LOAD);
        fileDialog.setVisible(true);

        try{
            String name = fileDialog.getFile();
            if(name.length() > 18)
                terrainData.setTerrainName(String.format("%.18s...",name));
            else
                terrainData.setTerrainName(name);
            loadTerrainData(fileDialog.getDirectory() + fileDialog.getFile(),terrainData,imageHelper);
        }catch(NullPointerException e){
            System.out.println("Input selection cancelled.");
            return false;
        }
        return true;
    }

    void loadTerrainData(String terrainPath,TerrainData terrainData,TerrainImageHelper imageHelper){
        BufferedImage terrainImage;
        try{
            terrainImage = ImageIO.read(new File(terrainPath));

            imageHelper.convertTerrainData(terrainImage,terrainData);
            terrainData.setTerrainPath(terrainPath);
        }catch(IOException e){
            System.out.println("Cannot read the specified file");
        }
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
        createUIComponents();
        cardPanel = new JPanel();
        cardPanel.setLayout(new CardLayout(0,0));
        panel0 = new JPanel();
        panel0.setLayout(new GridLayoutManager(3,2,new Insets(10,30,10,30),-1,-1));
        cardPanel.add(panel0,"Card4");
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null,-1,24,label1.getFont());
        if(label1Font != null) label1.setFont(label1Font);
        label1.setText("Help Window");
        panel0.add(label1,new GridConstraints(0,0,1,2,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        helpPanel = new JPanel();
        helpPanel.setLayout(new GridLayoutManager(1,1,new Insets(0,0,0,0),-1,-1));
        panel0.add(helpPanel,new GridConstraints(1,0,1,2,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        helpLabel = new JLabel();
        helpLabel.setText("");
        helpPanel.add(helpLabel,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        proceed0 = new JButton();
        Font proceed0Font = this.$$$getFont$$$(null,-1,16,proceed0.getFont());
        if(proceed0Font != null) proceed0.setFont(proceed0Font);
        proceed0.setText("Proceed");
        panel0.add(proceed0,new GridConstraints(2,1,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final Spacer spacer1 = new Spacer();
        panel0.add(spacer1,new GridConstraints(2,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_WANT_GROW,1,null,null,null,0,false));
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3,3,new Insets(10,30,10,30),-1,-1));
        panel1.setEnabled(true);
        cardPanel.add(panel1,"Card1");
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null,-1,24,label2.getFont());
        if(label2Font != null) label2.setFont(label2Font);
        label2.setText("Initial Window");
        panel1.add(label2,new GridConstraints(0,0,1,3,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1,3,new Insets(0,0,0,0),-1,-1,true,false));
        panel1.add(panel4,new GridConstraints(1,0,1,3,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        selectTerrainData1Button = new JButton();
        Font selectTerrainData1ButtonFont = this.$$$getFont$$$(null,-1,18,selectTerrainData1Button.getFont());
        if(selectTerrainData1ButtonFont != null) selectTerrainData1Button.setFont(selectTerrainData1ButtonFont);
        selectTerrainData1Button.setHorizontalTextPosition(11);
        selectTerrainData1Button.setText("Select Terrain Data 1");
        panel4.add(selectTerrainData1Button,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_FIXED,null,new Dimension(398,-1),new Dimension(398,-1),0,false));
        selectTerrainData2Button = new JButton();
        Font selectTerrainData2ButtonFont = this.$$$getFont$$$(null,-1,18,selectTerrainData2Button.getFont());
        if(selectTerrainData2ButtonFont != null) selectTerrainData2Button.setFont(selectTerrainData2ButtonFont);
        selectTerrainData2Button.setText("Select Terrain Data 2");
        panel4.add(selectTerrainData2Button,new GridConstraints(0,1,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_FIXED,null,new Dimension(398,-1),new Dimension(398,-1),0,false));
        helpButton = new JButton();
        Font helpButtonFont = this.$$$getFont$$$(null,-1,18,helpButton.getFont());
        if(helpButtonFont != null) helpButton.setFont(helpButtonFont);
        helpButton.setText("Help");
        panel4.add(helpButton,new GridConstraints(0,2,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_FIXED,null,new Dimension(398,-1),new Dimension(398,-1),0,false));
        proceed1 = new JButton();
        Font proceed1Font = this.$$$getFont$$$(null,-1,16,proceed1.getFont());
        if(proceed1Font != null) proceed1.setFont(proceed1Font);
        proceed1.setText("Proceed");
        panel1.add(proceed1,new GridConstraints(2,2,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2,new GridConstraints(2,1,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_WANT_GROW,1,null,null,null,0,false));
        panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3,3,new Insets(10,30,10,30),-1,-1));
        cardPanel.add(panel2,"Card2");
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null,-1,24,label3.getFont());
        if(label3Font != null) label3.setFont(label3Font);
        label3.setText("Main Window");
        panel2.add(label3,new GridConstraints(0,0,1,3,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1,3,new Insets(0,0,0,0),-1,-1,true,false));
        panel2.add(panel5,new GridConstraints(1,0,1,3,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_WANT_GROW,null,null,null,1,false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(3,1,new Insets(5,5,5,5),-1,-1));
        panel6.setBackground(new Color(-4473925));
        panel6.setEnabled(true);
        panel5.add(panel6,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        terrain1Name = new JLabel();
        Font terrain1NameFont = this.$$$getFont$$$(null,-1,18,terrain1Name.getFont());
        if(terrain1NameFont != null) terrain1Name.setFont(terrain1NameFont);
        terrain1Name.setText("Terrain 1 Name");
        terrain1Name.setToolTipText("Filename of the first terrain data");
        panel6.add(terrain1Name,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(9,2,new Insets(0,0,0,0),-1,-1));
        panel7.setBackground(new Color(-3618616));
        panel6.add(panel7,new GridConstraints(2,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null,-1,18,label4.getFont());
        if(label4Font != null) label4.setFont(label4Font);
        label4.setText("Dimension");
        label4.setToolTipText("This refers to the image width and height");
        panel7.add(label4,new GridConstraints(0,0,1,2,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null,-1,18,label5.getFont());
        if(label5Font != null) label5.setFont(label5Font);
        label5.setText("Levels");
        label5.setToolTipText("This refers to the height values being represented by the image pixel color");
        panel7.add(label5,new GridConstraints(4,0,1,2,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null,-1,14,label6.getFont());
        if(label6Font != null) label6.setFont(label6Font);
        label6.setText("Highest");
        panel7.add(label6,new GridConstraints(5,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        terrain1Highest = new JLabel();
        terrain1Highest.setEnabled(true);
        Font terrain1HighestFont = this.$$$getFont$$$(null,-1,14,terrain1Highest.getFont());
        if(terrain1HighestFont != null) terrain1Highest.setFont(terrain1HighestFont);
        terrain1Highest.setText("");
        panel7.add(terrain1Highest,new GridConstraints(5,1,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null,-1,14,label7.getFont());
        if(label7Font != null) label7.setFont(label7Font);
        label7.setText("Average");
        panel7.add(label7,new GridConstraints(6,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        terrain1Lowest = new JLabel();
        Font terrain1LowestFont = this.$$$getFont$$$(null,-1,14,terrain1Lowest.getFont());
        if(terrain1LowestFont != null) terrain1Lowest.setFont(terrain1LowestFont);
        terrain1Lowest.setText("");
        panel7.add(terrain1Lowest,new GridConstraints(7,1,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        terrain1Average = new JLabel();
        Font terrain1AverageFont = this.$$$getFont$$$(null,-1,14,terrain1Average.getFont());
        if(terrain1AverageFont != null) terrain1Average.setFont(terrain1AverageFont);
        terrain1Average.setText("");
        panel7.add(terrain1Average,new GridConstraints(6,1,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$(null,-1,14,label8.getFont());
        if(label8Font != null) label8.setFont(label8Font);
        label8.setText("Lowest");
        panel7.add(label8,new GridConstraints(7,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$(null,-1,14,label9.getFont());
        if(label9Font != null) label9.setFont(label9Font);
        label9.setText("Width");
        panel7.add(label9,new GridConstraints(1,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        terrain1Width = new JLabel();
        Font terrain1WidthFont = this.$$$getFont$$$(null,-1,14,terrain1Width.getFont());
        if(terrain1WidthFont != null) terrain1Width.setFont(terrain1WidthFont);
        terrain1Width.setText("");
        panel7.add(terrain1Width,new GridConstraints(1,1,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null,-1,14,label10.getFont());
        if(label10Font != null) label10.setFont(label10Font);
        label10.setText("Height");
        panel7.add(label10,new GridConstraints(2,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        terrain1Height = new JLabel();
        Font terrain1HeightFont = this.$$$getFont$$$(null,-1,14,terrain1Height.getFont());
        if(terrain1HeightFont != null) terrain1Height.setFont(terrain1HeightFont);
        terrain1Height.setText("");
        panel7.add(terrain1Height,new GridConstraints(2,1,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final Spacer spacer3 = new Spacer();
        panel7.add(spacer3,new GridConstraints(3,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_VERTICAL,1,GridConstraints.SIZEPOLICY_WANT_GROW,null,null,null,0,false));
        final Spacer spacer4 = new Spacer();
        panel7.add(spacer4,new GridConstraints(8,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_VERTICAL,1,GridConstraints.SIZEPOLICY_WANT_GROW,null,null,null,0,false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1,1,new Insets(0,0,0,0),-1,-1));
        panel6.add(panel8,new GridConstraints(1,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        terrainImageLabel1 = new JLabel();
        terrainImageLabel1.setText("");
        panel8.add(terrainImageLabel1,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,new Dimension(370,370),0,false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(3,1,new Insets(5,5,5,5),-1,-1));
        panel9.setBackground(new Color(-4473925));
        panel5.add(panel9,new GridConstraints(0,1,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        terrain2Name = new JLabel();
        Font terrain2NameFont = this.$$$getFont$$$(null,-1,18,terrain2Name.getFont());
        if(terrain2NameFont != null) terrain2Name.setFont(terrain2NameFont);
        terrain2Name.setText("Terrain 2 Name");
        terrain2Name.setToolTipText("Filename of the second terrain data");
        panel9.add(terrain2Name,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1,1,new Insets(0,0,0,0),-1,-1));
        panel9.add(panel10,new GridConstraints(1,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        terrainImageLabel2 = new JLabel();
        terrainImageLabel2.setText("");
        panel10.add(terrainImageLabel2,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,new Dimension(370,370),0,false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(9,2,new Insets(0,0,0,0),-1,-1));
        panel11.setBackground(new Color(-3618616));
        panel9.add(panel11,new GridConstraints(2,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null,-1,18,label11.getFont());
        if(label11Font != null) label11.setFont(label11Font);
        label11.setText("Dimension");
        label11.setToolTipText("This refers to the image width and height");
        panel11.add(label11,new GridConstraints(0,0,1,2,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$(null,-1,14,label12.getFont());
        if(label12Font != null) label12.setFont(label12Font);
        label12.setText("Width");
        panel11.add(label12,new GridConstraints(1,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        terrain2Width = new JLabel();
        Font terrain2WidthFont = this.$$$getFont$$$(null,-1,14,terrain2Width.getFont());
        if(terrain2WidthFont != null) terrain2Width.setFont(terrain2WidthFont);
        terrain2Width.setText("");
        panel11.add(terrain2Width,new GridConstraints(1,1,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        terrain2Height = new JLabel();
        Font terrain2HeightFont = this.$$$getFont$$$(null,-1,14,terrain2Height.getFont());
        if(terrain2HeightFont != null) terrain2Height.setFont(terrain2HeightFont);
        terrain2Height.setText("");
        panel11.add(terrain2Height,new GridConstraints(2,1,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label13 = new JLabel();
        Font label13Font = this.$$$getFont$$$(null,-1,14,label13.getFont());
        if(label13Font != null) label13.setFont(label13Font);
        label13.setText("Height");
        panel11.add(label13,new GridConstraints(2,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label14 = new JLabel();
        Font label14Font = this.$$$getFont$$$(null,-1,18,label14.getFont());
        if(label14Font != null) label14.setFont(label14Font);
        label14.setText("Levels");
        label14.setToolTipText("This refers to the height values being represented by the image pixel color");
        panel11.add(label14,new GridConstraints(4,0,1,2,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label15 = new JLabel();
        Font label15Font = this.$$$getFont$$$(null,-1,14,label15.getFont());
        if(label15Font != null) label15.setFont(label15Font);
        label15.setText("Highest");
        panel11.add(label15,new GridConstraints(5,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label16 = new JLabel();
        Font label16Font = this.$$$getFont$$$(null,-1,14,label16.getFont());
        if(label16Font != null) label16.setFont(label16Font);
        label16.setText("Average");
        panel11.add(label16,new GridConstraints(6,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label17 = new JLabel();
        Font label17Font = this.$$$getFont$$$(null,-1,14,label17.getFont());
        if(label17Font != null) label17.setFont(label17Font);
        label17.setText("Lowest");
        panel11.add(label17,new GridConstraints(7,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final Spacer spacer5 = new Spacer();
        panel11.add(spacer5,new GridConstraints(3,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_VERTICAL,1,GridConstraints.SIZEPOLICY_WANT_GROW,null,null,null,0,false));
        terrain2Highest = new JLabel();
        Font terrain2HighestFont = this.$$$getFont$$$(null,-1,14,terrain2Highest.getFont());
        if(terrain2HighestFont != null) terrain2Highest.setFont(terrain2HighestFont);
        terrain2Highest.setText("");
        panel11.add(terrain2Highest,new GridConstraints(5,1,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        terrain2Average = new JLabel();
        Font terrain2AverageFont = this.$$$getFont$$$(null,-1,14,terrain2Average.getFont());
        if(terrain2AverageFont != null) terrain2Average.setFont(terrain2AverageFont);
        terrain2Average.setText("");
        panel11.add(terrain2Average,new GridConstraints(6,1,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        terrain2Lowest = new JLabel();
        Font terrain2LowestFont = this.$$$getFont$$$(null,-1,14,terrain2Lowest.getFont());
        if(terrain2LowestFont != null) terrain2Lowest.setFont(terrain2LowestFont);
        terrain2Lowest.setText("");
        panel11.add(terrain2Lowest,new GridConstraints(7,1,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final Spacer spacer6 = new Spacer();
        panel11.add(spacer6,new GridConstraints(8,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_VERTICAL,1,GridConstraints.SIZEPOLICY_WANT_GROW,null,null,null,0,false));
        panelTerrainFeaturesMain = new JPanel();
        panelTerrainFeaturesMain.setLayout(new GridLayoutManager(5,1,new Insets(5,5,5,5),-1,-1));
        panelTerrainFeaturesMain.setBackground(new Color(-4473925));
        panel5.add(panelTerrainFeaturesMain,new GridConstraints(0,2,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label18 = new JLabel();
        Font label18Font = this.$$$getFont$$$(null,-1,18,label18.getFont());
        if(label18Font != null) label18.setFont(label18Font);
        label18.setText("Terrain Features");
        panelTerrainFeaturesMain.add(label18,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1,3,new Insets(0,0,0,0),-1,-1));
        panel12.setBackground(new Color(-3618616));
        panelTerrainFeaturesMain.add(panel12,new GridConstraints(1,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label19 = new JLabel();
        Font label19Font = this.$$$getFont$$$(null,-1,16,label19.getFont());
        if(label19Font != null) label19.setFont(label19Font);
        label19.setText("Feature");
        panel12.add(label19,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label20 = new JLabel();
        Font label20Font = this.$$$getFont$$$(null,-1,16,label20.getFont());
        if(label20Font != null) label20.setFont(label20Font);
        label20.setText("Detail");
        panel12.add(label20,new GridConstraints(0,1,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JLabel label21 = new JLabel();
        Font label21Font = this.$$$getFont$$$(null,-1,16,label21.getFont());
        if(label21Font != null) label21.setFont(label21Font);
        label21.setText("Weight");
        panel12.add(label21,new GridConstraints(0,2,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        panelTerrainFeaturesMain.add(comparisonPanel,new GridConstraints(2,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        addFeatureButton = new JButton();
        Font addFeatureButtonFont = this.$$$getFont$$$(null,-1,14,addFeatureButton.getFont());
        if(addFeatureButtonFont != null) addFeatureButton.setFont(addFeatureButtonFont);
        addFeatureButton.setText("Add");
        panelTerrainFeaturesMain.add(addFeatureButton,new GridConstraints(4,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        featureComboBox = new JComboBox();
        Font featureComboBoxFont = this.$$$getFont$$$(null,-1,14,featureComboBox.getFont());
        if(featureComboBoxFont != null) featureComboBox.setFont(featureComboBoxFont);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Level");
        defaultComboBoxModel1.addElement("Exclude Level");
        defaultComboBoxModel1.addElement("Max Horizontal");
        defaultComboBoxModel1.addElement("Max Vertical");
        defaultComboBoxModel1.addElement("Max Diagonal");
        defaultComboBoxModel1.addElement("Max Area");
        featureComboBox.setModel(defaultComboBoxModel1);
        panelTerrainFeaturesMain.add(featureComboBox,new GridConstraints(3,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        return2 = new JButton();
        Font return2Font = this.$$$getFont$$$(null,-1,16,return2.getFont());
        if(return2Font != null) return2.setFont(return2Font);
        return2.setText("Return");
        panel2.add(return2,new GridConstraints(2,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        proceed2 = new JButton();
        Font proceed2Font = this.$$$getFont$$$(null,-1,16,proceed2.getFont());
        if(proceed2Font != null) proceed2.setFont(proceed2Font);
        proceed2.setText("Proceed");
        panel2.add(proceed2,new GridConstraints(2,2,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final Spacer spacer7 = new Spacer();
        panel2.add(spacer7,new GridConstraints(2,1,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_WANT_GROW,1,null,null,null,0,false));
        panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3,2,new Insets(10,30,10,30),-1,-1));
        cardPanel.add(panel3,"Card3");
        main3Panel = new JPanel();
        main3Panel.setLayout(new GridLayoutManager(1,2,new Insets(0,0,0,0),-1,-1));
        panel3.add(main3Panel,new GridConstraints(1,0,1,2,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(2,1,new Insets(0,0,0,0),-1,-1,false,true));
        main3Panel.add(leftPanel,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(2,1,new Insets(5,5,5,5),-1,-1));
        panel13.setBackground(new Color(-4473925));
        leftPanel.add(panel13,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,new Dimension(300,300),0,false));
        resultsTerrain1Name = new JLabel();
        Font resultsTerrain1NameFont = this.$$$getFont$$$(null,-1,18,resultsTerrain1Name.getFont());
        if(resultsTerrain1NameFont != null) resultsTerrain1Name.setFont(resultsTerrain1NameFont);
        resultsTerrain1Name.setText("Terrain 1 Name");
        panel13.add(resultsTerrain1Name,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new GridLayoutManager(1,1,new Insets(0,0,0,0),-1,-1));
        panel13.add(panel14,new GridConstraints(1,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        resultsTerrain1 = new JLabel();
        resultsTerrain1.setText("");
        panel14.add(resultsTerrain1,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,new Dimension(300,300),0,false));
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new GridLayoutManager(2,1,new Insets(5,5,5,5),-1,-1));
        panel15.setBackground(new Color(-4473925));
        leftPanel.add(panel15,new GridConstraints(1,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,new Dimension(300,300),0,false));
        resultsTerrain2Name = new JLabel();
        Font resultsTerrain2NameFont = this.$$$getFont$$$(null,-1,18,resultsTerrain2Name.getFont());
        if(resultsTerrain2NameFont != null) resultsTerrain2Name.setFont(resultsTerrain2NameFont);
        resultsTerrain2Name.setText("Terrain 2 Name");
        panel15.add(resultsTerrain2Name,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new GridLayoutManager(1,1,new Insets(0,0,0,0),-1,-1));
        panel15.add(panel16,new GridConstraints(1,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        resultsTerrain2 = new JLabel();
        resultsTerrain2.setText("");
        panel16.add(resultsTerrain2,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,new Dimension(300,300),0,false));
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(3,1,new Insets(5,5,5,5),-1,0));
        rightPanel.setBackground(new Color(-4473925));
        main3Panel.add(rightPanel,new GridConstraints(0,1,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,new Dimension(890,-1),null,null,1,false));
        final JLabel label22 = new JLabel();
        label22.setBackground(new Color(-4473925));
        Font label22Font = this.$$$getFont$$$(null,-1,18,label22.getFont());
        if(label22Font != null) label22.setFont(label22Font);
        label22.setText("Comparison Results                                                        ");
        rightPanel.add(label22,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_WANT_GROW,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new GridLayoutManager(2,4,new Insets(0,0,0,0),0,0,true,false));
        panel17.setAlignmentY(0.5f);
        rightPanel.add(panel17,new GridConstraints(1,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_WANT_GROW,null,null,null,0,false));
        final JLabel label23 = new JLabel();
        Font label23Font = this.$$$getFont$$$(null,-1,16,label23.getFont());
        if(label23Font != null) label23.setFont(label23Font);
        label23.setText("Feature");
        panel17.add(label23,new GridConstraints(0,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,new Dimension(39,15),null,0,false));
        final JLabel label24 = new JLabel();
        Font label24Font = this.$$$getFont$$$(null,-1,16,label24.getFont());
        if(label24Font != null) label24.setFont(label24Font);
        label24.setText("Detail");
        panel17.add(label24,new GridConstraints(0,1,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,new Dimension(30,15),null,0,false));
        final JLabel label25 = new JLabel();
        Font label25Font = this.$$$getFont$$$(null,-1,16,label25.getFont());
        if(label25Font != null) label25.setFont(label25Font);
        label25.setText("Weight");
        panel17.add(label25,new GridConstraints(0,2,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,new Dimension(38,15),null,0,false));
        final JLabel label26 = new JLabel();
        Font label26Font = this.$$$getFont$$$(null,-1,16,label26.getFont());
        if(label26Font != null) label26.setFont(label26Font);
        label26.setText("Likeliness");
        panel17.add(label26,new GridConstraints(0,3,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,new Dimension(50,15),null,0,false));
        panel17.add(resultsPanel,new GridConstraints(1,0,1,4,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        overallPanel.setBackground(new Color(-13157828));
        rightPanel.add(overallPanel,new GridConstraints(2,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_BOTH,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,null,null,null,0,false));
        final JLabel label27 = new JLabel();
        Font label27Font = this.$$$getFont$$$(null,-1,24,label27.getFont());
        if(label27Font != null) label27.setFont(label27Font);
        label27.setText("Results Window");
        panel3.add(label27,new GridConstraints(0,0,1,2,GridConstraints.ANCHOR_WEST,GridConstraints.FILL_NONE,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,new Dimension(234,32),null,0,false));
        return3 = new JButton();
        Font return3Font = this.$$$getFont$$$(null,-1,16,return3.getFont());
        if(return3Font != null) return3.setFont(return3Font);
        return3.setText("Return");
        panel3.add(return3,new GridConstraints(2,0,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_FIXED,GridConstraints.SIZEPOLICY_FIXED,null,null,null,0,false));
        final Spacer spacer8 = new Spacer();
        panel3.add(spacer8,new GridConstraints(2,1,1,1,GridConstraints.ANCHOR_CENTER,GridConstraints.FILL_HORIZONTAL,GridConstraints.SIZEPOLICY_WANT_GROW,1,null,null,null,0,false));
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
        return cardPanel;
    }

    private void createUIComponents(){
        // TODO: place custom component creation code here
        comparisonPanel = new JPanel();
        comparisonPanel.setLayout(new BoxLayout(comparisonPanel,BoxLayout.Y_AXIS));

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel,BoxLayout.Y_AXIS));

        overallPanel = new JPanel();
        overallPanel.setLayout(new BoxLayout(overallPanel,BoxLayout.Y_AXIS));
    }
}
