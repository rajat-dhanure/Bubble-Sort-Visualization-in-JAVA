import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BubbleSortVisualization extends JPanel {
    private int[] array;
    private int[] arr;
    private int currentPass = 0;
    private int currentIndex = 0;
    private Timer timer;
    private int boxWidth = 50;
    private int boxHeight = 50;
    private int padding = 10;
    private Font passFont = new Font("Arial", Font.BOLD, 20);
    private Font titleFont = new Font("Arial", Font.BOLD, 30);
    private int[][] sortedArrays; // Store sorted arrays after each pass
    private boolean isSwapping = false;
    private int startX;
    private int startY;

    public BubbleSortVisualization() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPass < array.length - 1) {
                    bubbleSortStep();
                    repaint();
                } else {
                    timer.stop();
                }
            }
        });
    }
    public void setArray(int[] array) {
        this.array = array;
        currentPass = 0;
        sortedArrays = new int[array.length][];
        startX = (getWidth() - array.length * (boxWidth + padding)) / 2;
        startY = (getHeight() - boxHeight) / 2;
        arr = array.clone();
    }

    private void bubbleSortStep() {
        if (currentIndex < array.length - 1 - currentPass) {
            if (array[currentIndex] > array[currentIndex + 1]) {
                int temp = array[currentIndex];
                array[currentIndex] = array[currentIndex + 1];
                array[currentIndex + 1] = temp;
                isSwapping = true;
            }
            currentIndex++;
        }
        else {
            currentIndex = 0;
            sortedArrays[currentPass] = array.clone(); // Store the array after each pass
            currentPass++;
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw title "The Array Is" before the graph
        int graphWidth = array.length * (boxWidth + padding) - padding;
        int graphHeight = boxHeight + padding;
        int graphX = (getWidth() - graphWidth) / 2;
        int graphY = (getHeight() - graphHeight * currentPass) / 2;
        g.setColor(Color.BLACK);
        g.setFont(titleFont);
        FontMetrics titleMetrics = g.getFontMetrics(titleFont);
        String title = "Input Array is :";
        int titleX = (getWidth() - titleMetrics.stringWidth(title)) / 2;
        int titleY = (getHeight() - boxHeight) / 2 - 250;
        g.drawString(title, titleX, titleY);
        // Draw graphical representation of the array
        FontMetrics titleMetrics1 = g.getFontMetrics(titleFont);
        String title1 = "The Visualization of Bubble Sort";
        int titleX1 = (getWidth() - titleMetrics1.stringWidth(title1)) / 2;
        int titleY1 = (getHeight() - boxHeight) / 2 - 130;
        g.drawString(title1, titleX1, titleY1);

        for (int j = 0; j < array.length; j++) {
            int x1 = graphX + j * (boxWidth + padding);
            int y1 = graphHeight;

            g.setColor(Color.BLACK);

            g.fillRoundRect(x1, y1, boxWidth, boxHeight, 10, 10);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String numberStr1 = Integer.toString(arr[j]);
            int strX1 = x1 + (boxWidth - g.getFontMetrics().stringWidth(numberStr1)) / 2;
            int strY1 = y1 + (boxHeight - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
            g.drawString(numberStr1, strX1, strY1);
        }
        if (currentPass > 0) {

            g.setColor(Color.BLACK);
            g.setFont(titleFont);
            int pass = 0;

            for (pass = 0; pass < currentPass; pass++) {
                int[] sortedArray = sortedArrays[pass];
                for (int i = 0; i < array.length; i++) {
                    int x = graphX + i * (boxWidth + padding);
                    int y = graphY + pass * graphHeight;
                    // Highlight sorted elements

                    if (pass == currentPass - 1 && (i == currentIndex || i == currentIndex + 1)) {
                        g.setColor(Color.RED);
                    } else if (i >= array.length - 1 - pass || currentPass == array.length || ( pass== array.length-1)) {
                        g.setColor(Color.GREEN);
                    }
                    else {
                        g.setColor(Color.BLACK);
                    }
                    if (currentPass==array.length-1 && pass == currentPass-1){
                        for (int k = 0; k < array.length - 1; k++) {
                            g.setColor(Color.GREEN);
                        }
                    }
                    // Draw rounded rectangle box
                    g.fillRoundRect(x, y, boxWidth, boxHeight, 10, 10);

                    // Draw number inside the box
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 20));
                    String numberStr = Integer.toString(sortedArray[i]);
                    int strX = x + (boxWidth - g.getFontMetrics().stringWidth(numberStr)) / 2;
                    int strY = y + (boxHeight - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                    g.drawString(numberStr, strX, strY);
                }
            }
        }

        // Display current pass below the graph
        g.setColor(Color.BLACK);
        g.setFont(passFont);
        FontMetrics metrics = g.getFontMetrics(passFont);
        String passString = "Pass: " + currentPass;
        int passX = (getWidth() - metrics.stringWidth(passString)) / 2;
        int passY = (getHeight() - boxHeight) / 2 + 230;;
        g.drawString(passString, passX, passY);
    }

    public void startSorting() {
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Bubble Sort Visualization");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 700);
                frame.setLocationRelativeTo(null);

                JPanel panel = new JPanel();
                panel.setLayout(new FlowLayout());

                JLabel label = new JLabel("Enter array elements separated by spaces:");
                panel.add(label);

                JTextField textField = new JTextField(20);
                panel.add(textField);

                JButton button = new JButton("Start Sorting");
                panel.add(button);

                BubbleSortVisualization visualizationPanel = new BubbleSortVisualization();
                frame.add(visualizationPanel, BorderLayout.CENTER);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String input = textField.getText();
                        String[] elements = input.split("\\s+");
                        int[] array = new int[elements.length];
                        for (int i = 0; i < elements.length; i++) {
                            array[i] = Integer.parseInt(elements[i]);
                        }
                        visualizationPanel.setArray(array);
                        visualizationPanel.startSorting();
                    }
                });
                frame.add(panel, BorderLayout.NORTH);
                frame.setVisible(true);
            }
        });
    }
}
