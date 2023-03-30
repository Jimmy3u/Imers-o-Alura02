import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import java.io.InputStream;
import java.net.URI;

import javax.imageio.ImageIO;

public class CriadorDeFigurinhas {
    public void criaFigurinha(InputStream input, String nomeArquivo) throws Exception {
        // Lé a imagem para ser utilizada na figurinha
        BufferedImage imagemOri = ImageIO.read(input);

        // Pega o altura e largura da imagem e cria uma nova com transparencia e tamanho
        // aumentado
        int altura = imagemOri.getHeight();
        int largura = imagemOri.getWidth();
        int novaAltura = altura + 100;
        BufferedImage novaImg = new BufferedImage(largura, novaAltura, BufferedImage.TRANSLUCENT);

        // Pega o grafico da imagem e desenha a imagem original na nova imagem
        Graphics2D g2d = (Graphics2D) novaImg.createGraphics();
        g2d.drawImage(imagemOri, 0, 0, null);

        // Poem uma imagem dando joinha
        InputStream joinhaImg = URI.create("https://i.imgur.com/DoOvgh9.png").toURL().openStream();
        BufferedImage joinha = ImageIO.read(joinhaImg);
        //Gambiarra pra colocar num lugar mais ou menos no canto
        g2d.drawImage(joinha, -(joinha.getWidth() / 5), (altura - joinha.getHeight() / 2), null);
        // Cria um Objeto de fonte para trocar a que sera usada na imagem
        var fonte = new Font("Impact", Font.ROMAN_BASELINE, 64);
        g2d.setColor(Color.YELLOW);
        g2d.setFont(fonte);

        // Desenha o texto na imagem
        String texto ="Topzera";
        int larguraTexto = g2d.getFontMetrics().stringWidth(texto);
        g2d.drawString(texto, (largura-larguraTexto) / 2, novaAltura - 50);


        // Cria um outline
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout textTl = new TextLayout(texto, fonte, frc);
        Shape outline = textTl.getOutline(null);
       
        AffineTransform transform = g2d.getTransform();
        transform.translate((largura-larguraTexto) / 2, novaAltura - 50);

        g2d.setTransform(transform);
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.BLACK);
        g2d.draw(outline);
        // Salva a imagem

        ImageIO.write(novaImg, "png", new File(nomeArquivo));
    }
}