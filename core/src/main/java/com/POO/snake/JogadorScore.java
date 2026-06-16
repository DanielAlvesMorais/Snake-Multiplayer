package com.POO.snake;

public class JogadorScore {

    private String nome;
    private int pontos;

    public JogadorScore() {
    }

    public JogadorScore(String nome, int pontos) {
        this.nome = nome;
        this.pontos = pontos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Alterado para corresponder exatamente à propriedade 'pontos'
    public int getPoints() {
        return pontos;
    }

    public void setPoints(int pontos) {
        this.pontos = pontos;
    }
}
