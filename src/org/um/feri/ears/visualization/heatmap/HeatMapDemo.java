package org.um.feri.ears.visualization.heatmap;

import org.um.feri.ears.problems.unconstrained.Levy13;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <p>This class shows the various options of the HeatMap.</p>
 *
 * <hr />
 * <p><strong>Copyright:</strong> Copyright (c) 2007, 2008</p>
 *
 * <p>HeatMap is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.</p>
 *
 * <p>HeatMap is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.</p>
 *
 * <p>You should have received a copy of the GNU General Public License
 * along with HeatMap; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA</p>
 *
 * @author Matthew Beckler (matthew@mbeckler.org)
 * @author Josh Hayes-Sheen (grey@grevian.org), Converted to use BufferedImage.
 * @author J. Keller (jpaulkeller@gmail.com), Added transparency (alpha) support, data ordering bug fix.
 * @version 1.6
 */

class HeatMapDemo extends JFrame implements ItemListener, FocusListener
{
    HeatMap panel;
    JCheckBox drawLegend;
    JCheckBox drawTitle;
    JCheckBox drawXTitle;
    JCheckBox drawXTicks;
    JCheckBox drawYTitle;
    JCheckBox drawYTicks;
    JTextField textTitle;
    JTextField textXTitle;
    JTextField textYTitle;
    JTextField textXMin;
    JTextField textXMax;
    JTextField textYMin;
    JTextField textYMax;
    JTextField textFGColor;
    JTextField textBGColor;
    JComboBox gradientComboBox;
    JComboBox problemComboBox;

    int numberOfPartitions = 1000;
    int numberOfEvaluations = 30000; //TODO set from UI
    boolean useGraphicsYAxis = false;

    ImageIcon[] icons;
    String[] names = {"GRADIENT_BLACK_TO_WHITE",
                      "GRADIENT_BLUE_TO_RED",
                      "GRADIENT_GREEN_YELLOW_ORANGE_RED",
                      "GRADIENT_HEAT",
                      "GRADIENT_HOT",
                      "GRADIENT_MAROON_TO_GOLD",
                      "GRADIENT_RAINBOW",
                      "GRADIENT_RED_TO_GREEN",
                      "GRADIENT_ROY",
                      "GRADIENT_PARULA",
                      "GRADIENT_VIRDIS"};
    Color[][] gradients = {Gradient.GRADIENT_BLACK_TO_WHITE,
                           Gradient.GRADIENT_BLUE_TO_RED,
                           Gradient.GRADIENT_GREEN_YELLOW_ORANGE_RED,
                           Gradient.GRADIENT_HEAT,
                           Gradient.GRADIENT_HOT,
                           Gradient.GRADIENT_MAROON_TO_GOLD,
                           Gradient.GRADIENT_RAINBOW,
                           Gradient.GRADIENT_RED_TO_GREEN,
                           Gradient.GRADIENT_ROY,
                           Gradient.GRADIENT_PARULA,
                           Gradient.GRADIENT_VIRDIS};

    Problem[] problems = {
            new org.um.feri.ears.problems.unconstrained.cec2005.F01(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F02(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F03(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F04(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F05(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F06(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F07(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F08(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F09(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F10(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F11(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F12(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F13(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F14(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F15(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F16(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F17(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F18(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F19(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F20(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F21(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F22(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F23(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F24(2),
            new org.um.feri.ears.problems.unconstrained.cec2005.F25(2),
            new SplitDropWave1(),
            new SplitDropWave2(),
            new WayburnSeader3(),
            new Tsoulo(),
            new ThreeCylinders(),
            new Storn(),
            new Stenger(),
            new Schaffer6(),
            new Schaffer7(),
            new Sawtoothxy(),
            new S2(),
            new S3(),
            new Rump(),
            new Peaks(),
            new MullerBrown(),
            new Mineshaft3(),
            new Kearfott(),
            new H1(),
            new GramacyLee2(),
            new GramacyLee3(),
            new F26(),
            new Engvall(),
            new DownhillStep(),
            new Davis(),
            new Complex(),
            new ChensVfunction(),
            new ChensBird(),
            new Camel(),
            new BananaShape(),
            new HappyCat(),
            new Ackley2(),
            new Ackley3(),
            new Ackley4(),
            new Zimmerman(),
            new AluffiPentiniZirilli(),
            new YaoLiu4(),
            new YaoLiu9(),
            new XinSheYang1(),
            new XinSheYang2(),
            new XinSheYang3(),
            new XinSheYang4(),
            new Whitley(),
            new Weierstrass(),
            new WayburnSeader1(),
            new WayburnSeader2(),
            new Wavy(),
            new Vincent(),
            new VenterSobiezcczanskiSobieski(),
            new Ursem1(),
            new Ursem3(),
            new Ursem4(),
            new UrsemWaves(),
            new StretchedVSineWave(),
            new SineEnvelope(),
            new Shubert1(2),
            new Shubert3(2),
            new Shubert4(2),
            new Schwefel1(),
            new Schwefel12(),
            new Schwefel24(),
            new Schwefel26(),
            new Schwefel220(),
            new Schwefel221(),
            new Schwefel222(2),
            new Schwefel226(2),
            new Schwefel236(),
            new Schaffer1(),
            new Schaffer2(),
            new Schaffer3(),
            new Schaffer4(),
            new Sargan(),
            new Salomon(),
            new RotatedEllipse1(),
            new RotatedEllipse2(),
            new Ripple1(),
            new Ripple25(),
            new Rana(),
            new Quintic(),
            new Quadratic(),
            new Qing(),
            new Price1(),
            new Price2(),
            new Price3(),
            new Price4(),
            new Perm1(2),
            new Perm2(2),
            new Penalized(2),
            new Penalized2(2),
            new Parsopoulos(),
            new NeedleEye(),
            new Mishra1(),
            new Mishra2(),
            new Mishra3(),
            new Mishra4(),
            new Mishra5(),
            new Mishra6(),
            new Mishra7(),
            new Mishra8(),
            new Mishra10(),
            new Langermann(),
            new Keane(),
            new Katsuura(),
            new Judge(),
            new JennrichSampson(),
            new Hosaki(),
            new HimmelBlau(),
            new FreudensteinRoth(),
            new Exponential(),
            new BiggsExp2(),
            new ElAttarVidyasagarDutta(),
            new EggCrate(),
            new DeflectedCorrugatedSpring(),
            new DeckkersAarts(),
            new Deb1(),
            new Deb2(),
            new Damavandi(),
            new Cube(),
            new Csendes(),
            new CrownedCross(),
            new CrossLegTable(),
            new CrossInTray(),
            new Cigar(),
            new Chichinadze(),
            new CarromTable(),
            new Bukin2(),
            new Bukin4(),
            new Bukin6(),
            new Brown(),
            new Brent(),
            new Branin1(),
            new Branin2(),
            new Bird(),
            new BartelsConn(),
            new Mishra11(),
            new Alpine1(),
            new Alpine2(),
            new Adjiman(),
            new Zettl(),
            new ZeroSum(),
            new Trigonometric1(),
            new Trigonometric2(),
            new Trefethen(),
            new Treccani(),
            new TestTubeHolder(),
            new ReduxSum(),
            new Giunta(),
            new Tripod(),
            new PenHolder(),
            new Pathological(),
            new Plateau(),
            new StyblinskiTang(2),
            new Michalewicz2(),
            new DeJong5(),
            new ThreeHumpCamel(),
            new McCormick(),
            new Trid2(),
            new PowellSum(2),
            new Leon(),
            new Levy3(),
            new Levy5(),
            new Levy13(),
            new Bukin6(),
            new Eggholder(),
            new SharpRidge(2),
            new DifferentPowers(2),
            new LinearSlope(2),
            new Ackley1(2),
            new Beale(),
            new CrossInTray(),
            new DixonPrice(2),
            new DropWave(),
            new HolderTable(),
            new Langermann(),
            new ModifiedLangermann2(),
            new Matyas(),
            new Rastrigin(2),
            new RosenbrockDeJong2(2),
            new Schwefel226(2),
            new Schwefel226(2),
            new Shubert1(2),
            new Sphere(2),
            new SumSquares(2),
            new Zakharov(2)};

    Problem selectedProblem;

    public HeatMapDemo() throws Exception
    {
        super("Problem Heat Map");
        
        // gui stuff to demonstrate options
        JPanel listPane = new JPanel();
        listPane.setLayout(new GridBagLayout());
        listPane.setBorder(BorderFactory.createTitledBorder("Problem Heat Map"));

        GridBagConstraints gbc;        
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(2, 1, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.LINE_START;
        drawTitle = new JCheckBox("Draw Title");
        drawTitle.setSelected(true);
        drawTitle.addItemListener(this);
        listPane.add(drawTitle, gbc);
        gbc.gridy = GridBagConstraints.RELATIVE;
        
        drawLegend = new JCheckBox("Draw Legend");
        drawLegend.setSelected(true);
        drawLegend.addItemListener(this);
        listPane.add(drawLegend, gbc);
        
        drawXTitle = new JCheckBox("Draw X-Axis Title");
        drawXTitle.setSelected(true);
        drawXTitle.addItemListener(this);
        listPane.add(drawXTitle, gbc);
        
        drawXTicks = new JCheckBox("Draw X-Axis Ticks");
        drawXTicks.setSelected(true);
        drawXTicks.addItemListener(this);
        listPane.add(drawXTicks, gbc);
        
        drawYTitle = new JCheckBox("Draw Y-Axis Title");
        drawYTitle.setSelected(true);
        drawYTitle.addItemListener(this);
        listPane.add(drawYTitle, gbc);
        
        drawYTicks = new JCheckBox("Draw Y-Axis Ticks");
        drawYTicks.setSelected(true);
        drawYTicks.addItemListener(this);
        listPane.add(drawYTicks, gbc);
        
        listPane.add(Box.createVerticalStrut(10), gbc);
        
        JLabel label = new JLabel("Title:");
        listPane.add(label, gbc);
        
        textTitle = new JTextField();
        textTitle.addFocusListener(this);
        listPane.add(textTitle, gbc);
        
        label = new JLabel("X-Axis Title:");
        listPane.add(label, gbc);
        
        textXTitle = new JTextField();
        textXTitle.addFocusListener(this);
        listPane.add(textXTitle, gbc);

        label = new JLabel("Y-Axis Title:");
        listPane.add(label, gbc);

        textYTitle = new JTextField();
        textYTitle.addFocusListener(this);
        listPane.add(textYTitle, gbc);
        
        listPane.add(Box.createVerticalStrut(10), gbc);
        
        //14 is next row number
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        label = new JLabel("X min:");
        gbc.gridx = 0;
        gbc.gridy = 14;
        listPane.add(label, gbc);
        textXMin = new JTextField();
        textXMin.addFocusListener(this);
        gbc.gridy = 15;
        listPane.add(textXMin, gbc);
        
        label = new JLabel("X max:");
        gbc.gridx = 1;
        gbc.gridy = 14;
        listPane.add(label, gbc);
        textXMax = new JTextField();
        textXMax.addFocusListener(this);
        gbc.gridy = 15;
        listPane.add(textXMax, gbc);
        
        label = new JLabel("Y min:");
        gbc.gridx = 2;
        gbc.gridy = 14;
        listPane.add(label, gbc);
        textYMin = new JTextField();
        textYMin.addFocusListener(this);
        gbc.gridy = 15;
        listPane.add(textYMin, gbc);
        
        label = new JLabel("Y max:");
        gbc.gridx = 3;
        gbc.gridy = 14;
        listPane.add(label, gbc);
        textYMax = new JTextField();
        textYMax.addFocusListener(this);
        gbc.gridy = 15;
        listPane.add(textYMax, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 16;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        listPane.add(Box.createVerticalStrut(10), gbc);
        
        label = new JLabel("FG Color:");
        gbc.gridx = 0;
        gbc.gridy = 17;
        gbc.gridwidth = 2;
        listPane.add(label, gbc);
        textFGColor = new JTextField();
        textFGColor.addFocusListener(this);
        gbc.gridy = 18;
        listPane.add(textFGColor, gbc);
        
        label = new JLabel("BG Color:");
        gbc.gridx = 2;
        gbc.gridy = 17;
        listPane.add(label, gbc);
        textBGColor = new JTextField();
        textBGColor.addFocusListener(this);
        gbc.gridy = 18;
        listPane.add(textBGColor, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        listPane.add(Box.createVerticalStrut(10), gbc);

        //----------------------------------------------------------------------

        label = new JLabel("Gradient:");
        listPane.add(label, gbc);

        icons = new ImageIcon[names.length];
        Integer[] intArray = new Integer[names.length];
        for (int i = 0; i < names.length; i++)
        {
            intArray[i] = new Integer(i);
            icons[i] = createImageIcon(names[i] + ".gif");
        }

        gradientComboBox = new JComboBox(intArray);
        ComboBoxRenderer renderer = new ComboBoxRenderer();
        gradientComboBox.setRenderer(renderer);
        gradientComboBox.addItemListener(this);

        listPane.add(gradientComboBox, gbc);

        //----------------------------------------------------------------------

        label = new JLabel("Problem:");
        listPane.add(label, gbc);

        Integer[] selectArray = new Integer[problems.length];
        for (int i = 0; i < problems.length; i++)
        {
            selectArray[i] = i;
        }

        problemComboBox = new JComboBox(selectArray);
        ProblemCbRenderer cbRenderer = new ProblemCbRenderer();
        problemComboBox.setRenderer(cbRenderer);
        problemComboBox.addItemListener(this);

        listPane.add(problemComboBox, gbc);

        //----------------------------------------------------------------------

        JButton OKButton = new JButton("Run EA");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Running");
                // set max eval selectedProblem
                panel.drawData(); //repaint the heatmap to clear previous population
                //TODO run EA on selected problem and draw population with drawPoint method
                //TODO map solution x values to x and y coordinates on the heatmap
                //TODO check useGraphicsYAxis
                panel.drawPoint(400, 400, 10, Color.magenta);
            }
        });

        listPane.add(OKButton,gbc);

        //----------------------------------------------------------------------

        problemComboBox.setSelectedIndex(0);
        selectedProblem = problems[0];
        double[][] data =  ProblemFitnessLandscape.GetProblemHeatmapValues(selectedProblem.getName(), numberOfPartitions);

        // you can use a pre-defined gradient:
        panel = new HeatMap(data, useGraphicsYAxis, Gradient.GRADIENT_RAINBOW);
        gradientComboBox.setSelectedIndex(6);

        // set miscellaneous settings
        panel.setDrawLegend(true);

        panel.setDrawTitle(true);

        panel.setXAxisTitle("X1");
        textXTitle.setText("X1");
        panel.setDrawXAxisTitle(true);

        panel.setYAxisTitle("X2");
        textYTitle.setText("X2");
        panel.setDrawYAxisTitle(true);

        panel.setTitle(selectedProblem.getName());
        textTitle.setText(selectedProblem.getName());

        panel.setCoordinateBounds(selectedProblem.lowerLimit.get(0), selectedProblem.upperLimit.get(0), selectedProblem.lowerLimit.get(1), selectedProblem.upperLimit.get(1));

        textXMin.setText(selectedProblem.lowerLimit.get(0)+"");
        textXMax.setText(selectedProblem.upperLimit.get(0)+"");
        textYMin.setText(selectedProblem.lowerLimit.get(1)+"");
        textYMax.setText(selectedProblem.upperLimit.get(1)+"");

        panel.setDrawXTicks(true);
        panel.setDrawYTicks(true);

        panel.setColorForeground(Color.black);
        textFGColor.setText("000000");
        panel.setColorBackground(Color.white);
        textBGColor.setText("FFFFFF");
        
        
	    this.getContentPane().add(listPane, BorderLayout.EAST);
        this.getContentPane().add(panel, BorderLayout.CENTER);
    }
    
    public void focusGained(FocusEvent e)
    {}
    
    public void focusLost(FocusEvent e)
    {
        Object source = e.getSource();
        
        if (source == textTitle)
        {
            panel.setTitle(textTitle.getText());
        }
        else if (source == textXTitle)
        {
            panel.setXAxisTitle(textXTitle.getText());
        }
        else if (source == textYTitle)
        {
            panel.setYAxisTitle(textYTitle.getText());
        }
        else if (source == textXMin)
        {
            double d;
            try
            {
                d = Double.parseDouble(textXMin.getText());
                panel.setXMinCoordinateBounds(d);
            }
            catch (Exception ex){}
        }
        else if (source == textXMax)
        {
            double d;
            try
            {
                d = Double.parseDouble(textXMax.getText());
                panel.setXMaxCoordinateBounds(d);
            }
            catch (Exception ex){}
        }
        else if (source == textYMin)
        {
            double d;
            try
            {
                d = Double.parseDouble(textYMin.getText());
                panel.setYMinCoordinateBounds(d);
            }
            catch (Exception ex){}
        }
        else if (source == textYMax)
        {
            double d;
            try
            {
                d = Double.parseDouble(textYMax.getText());
                panel.setYMaxCoordinateBounds(d);
            }
            catch (Exception ex){}
        }
        else if (source == textFGColor)
        {
            String c = textFGColor.getText();
            if (c.length() != 6)
                return;
            
            Color color = colorFromHex(c);
            if (color == null)
                return;

            panel.setColorForeground(color);
        }
        else if (source == textBGColor)
        {
            String c = textBGColor.getText();
            if (c.length() != 6)
                return;

            Color color = colorFromHex(c);
            if (color == null)
                return;

            panel.setColorBackground(color);
        }
        
    }
    
    private Color colorFromHex(String c)
    {
        try
        {
            int r = Integer.parseInt(c.substring(0, 2), 16);
            int g = Integer.parseInt(c.substring(2, 4), 16);
            int b = Integer.parseInt(c.substring(4, 6), 16);
            
            float rd = r / 255.0f;
            float gd = g / 255.0f;
            float bd = b / 255.0f;
            
            return new Color(rd, gd, bd);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    public void itemStateChanged(ItemEvent e)
    {
    	Object source = e.getItemSelectable();

        if (source == drawLegend)
        {
            panel.setDrawLegend(e.getStateChange() == ItemEvent.SELECTED);
        }
        else if (source == drawTitle)
        {
            panel.setDrawTitle(e.getStateChange() == ItemEvent.SELECTED);
        }
        else if (source == drawXTitle)
        {
            panel.setDrawXAxisTitle(e.getStateChange() == ItemEvent.SELECTED);
        }
        else if (source == drawXTicks)
        {
            panel.setDrawXTicks(e.getStateChange() == ItemEvent.SELECTED);
        }
        else if (source == drawYTitle)
        {
            panel.setDrawYAxisTitle(e.getStateChange() == ItemEvent.SELECTED);
        }
        else if (source == drawYTicks)
        {
            panel.setDrawYTicks(e.getStateChange() == ItemEvent.SELECTED);
        }
        else if (source == gradientComboBox)
        {
            // must be from the combo box
            Integer ix = (Integer) e.getItem();
            if (e.getStateChange() == ItemEvent.SELECTED)
            {
                panel.updateGradient(gradients[ix]);
            }
        }
        else if(source == problemComboBox) {
            Integer ix = (Integer) e.getItem();
            selectedProblem = problems[ix];

            updateProblemData();
        }
    }

    private void updateProblemData() {
        if(panel == null)
            return;

        double[][] data =  ProblemFitnessLandscape.GetProblemHeatmapValues(selectedProblem.getName(), numberOfPartitions);
        panel.updateData(data, useGraphicsYAxis);

        panel.setTitle(selectedProblem.getName());
        textTitle.setText(selectedProblem.getName());

        panel.setCoordinateBounds(selectedProblem.lowerLimit.get(0), selectedProblem.upperLimit.get(0), selectedProblem.lowerLimit.get(1), selectedProblem.upperLimit.get(1));

        textXMin.setText(selectedProblem.lowerLimit.get(0)+"");
        textXMax.setText(selectedProblem.upperLimit.get(0)+"");
        textYMin.setText(selectedProblem.lowerLimit.get(1)+"");
        textYMax.setText(selectedProblem.upperLimit.get(1)+"");
    }
    
    // this function will be run from the EDT
    private static void createAndShowGUI() throws Exception
    {
        HeatMapDemo hmd = new HeatMapDemo();
        hmd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hmd.setSize(1200, 800);
        hmd.setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    createAndShowGUI();
                }
                catch (Exception e)
                {
                    System.err.println(e);
                    e.printStackTrace();
                }
            }
        });
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path)
    {
        ClassLoader classLoader = getClass().getClassLoader();
        java.net.URL imgURL = classLoader.getResource(path);

        if (imgURL != null)
        {
            return new ImageIcon(imgURL);
        }
        else
        {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    class ProblemCbRenderer extends JLabel implements ListCellRenderer
    {
        public ProblemCbRenderer()
        {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus)
        {
            int selectedIndex = ((Integer)value).intValue();
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setText(problems[selectedIndex].getName());
            return this;
        }
    }

    
    class ComboBoxRenderer extends JLabel implements ListCellRenderer
    {
        public ComboBoxRenderer()
        {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }
        
        public Component getListCellRendererComponent(
                                                JList list,
                                                Object value,
                                                int index,
                                                boolean isSelected,
                                                boolean cellHasFocus)
        {
            int selectedIndex = ((Integer)value).intValue();
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            ImageIcon icon = icons[selectedIndex];
            setIcon(icon);
            setText(names[selectedIndex].substring(9));
            return this;
        }
    }
}
