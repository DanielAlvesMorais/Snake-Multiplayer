package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Rank {

    // Sua lista Java que guarda os dados na memória RAM enquanto o jogo roda
    private ArrayList<JogadorScore> listaRanking;
    private Json json;
    private String name; // Variável para armazenar o nome do jogador

    public Rank() {
        this.json = new Json();
        this.listaRanking = new ArrayList<>();
        carregarDoArquivo();
    }

    private void carregarDoArquivo() {
        var arquivo = Gdx.files.local("ranking.json");
        if (arquivo.exists()) {
            String texto = arquivo.readString();
            // O LibGDX converte o JSON direto para um ArrayList de uma classe sua
            this.listaRanking = json.fromJson(ArrayList.class, JogadorScore.class, texto);
        }
    }

    // A verificação é feita direto na memória RAM! Super rápido.
    public void verificarEAdicionarNovoScore(String nome, int pontos) {
        // 1. Adiciona o novo score na lista Java
        listaRanking.add(new JogadorScore(nome, pontos));

        // 2. Ordena a lista do maior para o menor score
        Collections.sort(listaRanking, new Comparator<JogadorScore>() {
            @Override
            public int compare(JogadorScore o1, JogadorScore o2) {
                return Integer.compare(o2.getPoints(), o1.getPoints()); // Decrescente
            }
        });

        // 3. Opcional: Define o limite de quantos scores manter no ranking (ex: top 5)
        if (listaRanking.size() > 10) {
            listaRanking.remove(listaRanking.size() - 1);
        }

        // 4. Salva no arquivo apenas quando a lista mudar!
        salvarNoArquivo();
    }

    public String getPlayerName(){
        
        return this.name;
    }

    private void salvarNoArquivo() {
        String textoJson = json.prettyPrint(listaRanking); // Transforma a lista em texto JSON
        Gdx.files.local("ranking.json").writeString(textoJson, false);
    }

    public ArrayList<JogadorScore> getListaRanking() {
        return this.listaRanking;
    }

    public void resetarRanking() {
        this.listaRanking.clear();
        salvarNoArquivo();
    }

}
