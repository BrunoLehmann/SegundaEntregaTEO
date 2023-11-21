package InterfazGrafica;

import Analizadores.Lexico;
import Analizadores.parser;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Consola extends JFrame {
    private JButton analizarButton1;
    private JButton cargarArchivoButton;
    private JTextArea textEntrada;
    private JTextArea textSalidaSintactico;
    private JPanel mainPanel;
    private JScrollPane panelEntrada;
    private JScrollPane panelSalidaSintactico;
    private JScrollPane panelSalidaLexico;
    private JTextArea textSalidaLexico;
    private String pathArchivo = "default.txt";
    private BuscadorArchivo buscador = new BuscadorArchivo(this);


    public Consola() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800,800);
        setVisible(false);
        buscador.setVisible(false);

        analizarButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    pathArchivo = buscador.getNombreArchivo();
                    analizarEntrada(pathArchivo);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        cargarArchivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscador.setVisible(true);
                setVisible(false);
            }

        });
    }

    public void start(){
        setVisible(true);
    }

    public void analizarEntrada(String nombreArchivo) throws IOException {
        String codigo = textEntrada.getText();
        textSalidaSintactico.setText("");
        File archivo = new File(nombreArchivo);

        FileReader f = new FileReader(nombreArchivo); //Proceso el archivo con el analizador lexico y lo muestro en textSalidaSintactico
        Lexico lexer = new Lexico(f);
        parser sintactico = new parser(lexer);
        archivo.createNewFile();
        FileWriter writer = new FileWriter(archivo);
        writer.write(codigo);   //Escribo la entrada del TextArea en un archivo.
        writer.close();

        try {
            sintactico.parse();
            String resultadoAnalizadorLexico = lexer.retornarStr();
            String resultErroresLexico = lexer.retornarErrores();
            String resultAnalizadorSintactico = sintactico.res;
            textSalidaSintactico.setText(resultAnalizadorSintactico);
            if(resultErroresLexico.isEmpty()){
                textSalidaLexico.setText(resultadoAnalizadorLexico);
            }else{
                textSalidaLexico.setText(resultadoAnalizadorLexico + "\n" + "ERRORES" + "\n" + resultErroresLexico);
            }

        } catch (IOException e) {
            textSalidaSintactico.setText("Archivo no encontrado!.");
        } catch (Exception e) {
            textSalidaSintactico.setText(sintactico.res);
            textSalidaLexico.setText(lexer.retornarErrores());
            throw new RuntimeException(e);
        }

    }

    public void setEntrada(String s){
        textEntrada.setText(s);
    }

    public void setPathArchivo(String pathArchivo) {
        this.pathArchivo = pathArchivo;
    }
}
