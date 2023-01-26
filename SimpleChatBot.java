import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SimpleChatBot extends JFrame implements ActionListener {

    //Задаємо розміри та обєкти інтерфейсу програми
    final String PROGRAM_TITLE = "Help:";
    final int START_LOCATION = 200;
    final int WINDOW_WIDTH = 350;
    final int WINDOW_HEIGHT = 450;

    JTextArea dialogue; // область взаємодії з ботом
    JCheckBox ai;       // чекбокс
    JTextField message; // поле для тексту
    SimpleBot sbot;     // клас

    public static void main(String[] args) {
        new SimpleChatBot();
    }

    SimpleChatBot() {
        setTitle(PROGRAM_TITLE); //заголовок програми
        setDefaultCloseOperation(EXIT_ON_CLOSE); //закриття програми
        setBounds(START_LOCATION, START_LOCATION, WINDOW_WIDTH, WINDOW_HEIGHT);// координати вікна проограми
        dialogue = new JTextArea();// створюємо область для діалогу
        dialogue.setText("Напишіть що вам підсказати \n");// текст на початку
        dialogue.setLineWrap(true);// строки будуть переноситися
        JScrollPane scrollBar = new JScrollPane(dialogue); //створюємо скроллбар (для вертикального скролінгу)
        JPanel bp = new JPanel(); //створення панелі
        bp.setLayout(new BoxLayout(bp, BoxLayout.X_AXIS)); //кнопка
        ai = new JCheckBox("ai"); //прапорець
        ai.doClick();//кнопка ai буде увімкнена при запуску бота
        message = new JTextField(); //поле для тексту
        message.addActionListener(this);
        JButton enter = new JButton("Enter");//кнопка для відправки повідомлення
        enter.addActionListener(this);//відправка повідомлення через клавіатуру
        bp.add(ai);//додаємо елементи до панелі bp
        bp.add(message);
        bp.add(enter);
        add(BorderLayout.SOUTH, bp);
        add(BorderLayout.CENTER, scrollBar);
        setVisible(true); //робимо все що було написано вище видимим
        sbot = new SimpleBot();
    }

    @Override
    public void actionPerformed(ActionEvent event) { //додавання тексту через поле,перевірка поля message
        if (message.getText().trim().length() > 0) {
            dialogue.append(message.getText() + "\n");
            dialogue.append(PROGRAM_TITLE.substring(0, 5) +
                    sbot.sayInReturn(message.getText(), ai.isSelected()) + "\n");
        }
        message.setText("");
        message.requestFocusInWindow();
    }
}
