
import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

public class PrettyCalc {

    static String current = "";
    static double first = 0;
    static String operator = "";
    static boolean fresh = true;

    static JLabel display;
    static JLabel expression;

    static final Color BACKGROUND = new Color(250, 225, 245);
    static final Color CREAM = new Color(255, 250, 245);
    static final Color PURPLE = new Color(195, 160, 225);
    static final Color PINK = new Color(240, 140, 195);
    static final Color TEXT = new Color(105, 65, 110);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("PrettyCalc ♡");
            frame.setSize(370, 510);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setAlwaysOnTop(false);

            try {
                Image icon = ImageIO.read(new File("calculator.png"));
                frame.setIconImage(icon);
            } catch (Exception e) {
                System.out.println(
                    "calculator.png not found. Using default icon."
                );
            }

            JPanel main = new JPanel(new BorderLayout(10, 12));
            main.setBackground(BACKGROUND);
            main.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            );

            JLabel title = new JLabel(
                "♡ PrettyCalc ♡", SwingConstants.CENTER
            );
            title.setFont(new Font("SansSerif", Font.BOLD, 25));
            title.setForeground(new Color(145, 90, 175));

            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(BACKGROUND);
            titlePanel.add(title, BorderLayout.CENTER);

            expression = new JLabel(" ", SwingConstants.RIGHT);
            expression.setFont(
                new Font("SansSerif", Font.PLAIN, 17)
            );
            expression.setForeground(new Color(160, 125, 160));

            display = new JLabel("0", SwingConstants.RIGHT);
            display.setFont(
                new Font("SansSerif", Font.BOLD, 34)
            );
            display.setForeground(TEXT);

            JPanel screen = new JPanel(new GridLayout(2, 1, 0, 5));
            screen.setBackground(CREAM);
            screen.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                        new Color(235, 195, 220), 2
                    ),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                )
            );

            screen.add(expression);
            screen.add(display);

            JPanel buttons = new JPanel(
                new GridLayout(5, 4, 9, 9)
            );
            buttons.setBackground(BACKGROUND);

            String[] keys = {
                "AC", "CE", "%", "÷",
                "7", "8", "9", "×",
                "4", "5", "6", "−",
                "1", "2", "3", "+",
                "0", ".", "=", "⌫"
            };

            for (String key : keys) {
                JButton button = new JButton(key);

                button.setFont(
                    new Font("SansSerif", Font.BOLD, 20)
                );
                button.setFocusPainted(false);
                button.setOpaque(true);
                button.setBorderPainted(false);
                button.setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
                );

                if ("÷×−+".contains(key)) {
                    button.setBackground(PURPLE);
                } else if (key.equals("=")) {
                    button.setBackground(PINK);
                } else {
                    button.setBackground(CREAM);
                }

                button.setForeground(TEXT);

                button.addMouseListener(
                    new java.awt.event.MouseAdapter() {
                        public void mouseEntered(
                            java.awt.event.MouseEvent e
                        ) {
                            button.setBackground(
                                new Color(245, 195, 225)
                            );
                        }

                        public void mouseExited(
                            java.awt.event.MouseEvent e
                        ) {
                            if ("÷×−+".contains(key)) {
                                button.setBackground(PURPLE);
                            } else if (key.equals("=")) {
                                button.setBackground(PINK);
                            } else {
                                button.setBackground(CREAM);
                            }
                        }
                    }
                );

                button.addActionListener(e -> press(key));
                buttons.add(button);
            }

            main.add(titlePanel, BorderLayout.NORTH);

            JPanel center = new JPanel(new BorderLayout(0, 12));
            center.setBackground(BACKGROUND);
            center.add(screen, BorderLayout.NORTH);
            center.add(buttons, BorderLayout.CENTER);

            main.add(center, BorderLayout.CENTER);

            frame.setContentPane(main);
            frame.setVisible(true);
        });
    }

    static void press(String key) {

        if (key.matches("[0-9]")) {
            if (fresh || current.equals("0")) {
                current = key;
                fresh = false;
            } else {
                current += key;
            }

            display.setText(current);
            updateExpression();

        } else if (key.equals(".")) {
            if (fresh) {
                current = "0";
                fresh = false;
            }

            if (!current.contains(".")) {
                current += ".";
            }

            display.setText(current);
            updateExpression();

        } else if (key.equals("AC")) {
            current = "";
            first = 0;
            operator = "";
            fresh = true;

            expression.setText(" ");
            display.setText("0");

        } else if (key.equals("CE")) {
            current = "";
            fresh = true;
            display.setText("0");
            updateExpression();

        } else if (key.equals("⌫")) {
            if (!fresh && !current.isEmpty()) {
                current = current.substring(
                    0, current.length() - 1
                );

                if (current.isEmpty() || current.equals("-")) {
                    current = "0";
                }

                display.setText(current);
                updateExpression();
            }

        } else if (key.equals("%")) {
            try {
                double n = Double.parseDouble(
                    current.isEmpty() ? "0" : current
                );

                current = format(n / 100);
                display.setText(current);
                fresh = true;
                updateExpression();

            } catch (NumberFormatException ex) {
                display.setText("Error");
            }

        } else if ("+−×÷".contains(key)) {
            try {
                first = Double.parseDouble(
                    current.isEmpty() ? "0" : current
                );

                operator = key;
                expression.setText(format(first) + " " + operator);

                current = "";
                fresh = true;

            } catch (NumberFormatException ex) {
                display.setText("Error");
            }

        } else if (key.equals("=")) {
            if (operator.isEmpty()) {
                return;
            }

            try {
                double second = Double.parseDouble(
                    current.isEmpty() ? "0" : current
                );

                expression.setText(
                    format(first) + " " + operator + " "
                    + format(second)
                );

                double result;

                switch (operator) {
                    case "+":
                        result = first + second;
                        break;

                    case "−":
                        result = first - second;
                        break;

                    case "×":
                        result = first * second;
                        break;

                    case "÷":
                        if (second == 0) {
                            display.setText("Cannot divide by 0");
                            current = "";
                            operator = "";
                            fresh = true;
                            return;
                        }

                        result = first / second;
                        break;

                    default:
                        return;
                }

                current = format(result);
                display.setText(current);

                operator = "";
                fresh = true;

            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        }
    }

    static void updateExpression() {
        if (!operator.isEmpty()) {
            expression.setText(
                format(first) + " " + operator
                + (current.isEmpty() ? "" : " " + current)
            );
        }
    }

    static String format(double n) {
        if (Double.isFinite(n)
                && n == Math.rint(n)
                && Math.abs(n) < 1.0e18) {
            return String.valueOf((long) n);
        }

        return String.valueOf(n);
    }
}
