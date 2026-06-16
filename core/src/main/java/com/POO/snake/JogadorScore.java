package com.POO.snake;

// Esta classe serve como o modelo (blueprint) para cada linha do seu ranking
public class JogadorScore {
    
    private String nome;
    private int pontos;

    // O construtor vazio é OBRIGATÓRIO para o Json do LibGDX conseguir reconstruir o objeto
    public JogadorScore() {
    }

    public JogadorScore(String nome, int pontos) {
        this.nome = nome;
        this.pontos = pontos;
    }

    // Getters e Setters (o componente Json também precisa dos setters para injetar os dados do arquivo)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPoints() { // Mantendo o padrão ou altere para getPontos se preferir
        return pontos;
    }

    public void setPoints(int pontos) {
        this.pontos = pontos;
    }
}