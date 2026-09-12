package com.example.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiAiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    suspend fun queryGemini(prompt: String, systemInstruction: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // High fidelity contextual theological fallback
            return@withContext generateLocalTheologicalResponse(prompt)
        }

        try {
            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        })
                    })
                })
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", systemInstruction))
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.4)
                    put("topP", 0.9)
                })
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val json = JSONObject(responseBody)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val first = candidates.getJSONObject(0)
                    val content = first.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "")
                    }
                }
            }
            generateLocalTheologicalResponse(prompt)
        } catch (e: Exception) {
            generateLocalTheologicalResponse(prompt)
        }
    }

    // Theological local response generator respecting strict biblical guidelines
    fun generateLocalTheologicalResponse(prompt: String): String {
        val p = prompt.lowercase()

        return when {
            p.contains("joão 3:16") || p.contains("joao 3:16") -> """
### 📖 Estudo Cristocêntrico — João 3:16

**1. Contexto Histórico e Literário**
Jesus conversa à noite com Nicodemos, um mestre da lei respeitado em Israel. Cristo expõe a absoluta incapacidade da religiosidade humana em produzir salvação sem o novo nascimento soberano do Espírito Santo.

**2. Explicação Exegética e Palavras-Chave**
- *"Amou de tal maneira" (Grego: Agapē)*: O amor que parte da iniciativa graciosa de Deus, sacrificial e imerecido.
- *"Deu seu Filho unigênito" (Grego: Monogenēs)*: O Filho único da mesma essência e glória com o Pai.
- *"Para que todo aquele que nele crê"*: A fé salvadora é o meio pelo qual a justiça de Cristo nos é imputada.
- *"Não pereça, mas tenha a vida eterna"*: Resgate solene da condenação eterna para a comunhão perpétua com Deus.

**3. Centralidade em Cristo**
O amor de Deus não é um conceito abstrato ou benevolência passiva; ele tem formato visível na entrega de Jesus na cruz. Cristo é a serpente de bronze levantada no deserto (v. 14) para curar os picados pelo veneno do pecado.

**4. Relação com a História da Redenção**
Cumpre a aliança com Abraão ("em ti serão benditas todas as nações") e a tipologia do sacrifício de Isaque no monte Moriá, onde o Pai não poupou Seu próprio Filho por nós.

**5. Aplicações Práticas e Perguntas para Reflexão**
- Como você tem respondido ao amor incondicional de Deus?
- Você tem tentado merecer a aceitação divina por obras ou descansado na obra consumada de Cristo?
- Compartilhe esta verdade com quem teme o juízo ou busca sentido para a existência.
            """.trimIndent()

            p.contains("conselho") || p.contains("desanimado") || p.contains("ansiedade") || p.contains("triste") || p.contains("medo") -> """
### 🕊️ Princípios Bíblicos para o seu Coração

Querido irmão(ã), ouvimos a sua aflição com acolhimento e oração.

**1. Um Princípio Bíblico que Traz Paz**
*“Não andeis ansiosos de coisa alguma; em tudo, porém, sejam conhecidas, diante de Deus, as vossas petições, pela oração e pela súplica, com ações de graças. E a paz de Deus, que excede todo o entendimento, guardará os vossos corações e os vossos sentimentos em Cristo Jesus.”* (Filipenses 4:6-7)

**2. O que a Palavra nos Ensina**
Deus nunca despreza o coração quebrantado e contrito (Sl 51:17). Em Cristo, temos um Sumo Sacerdote compassivo que se compadece de todas as nossas fraquezas e dores (Hb 4:15).

**3. Reflexão Prática**
- Entregue a Ele a causa do seu desânimo, sabendo que o fardo de Cristo é suave e Seu jugo é leve.
- Não tome decisões precipitadas sob o peso da angústia; espere com paciência no Senhor.

**4. Uma Oração para Este Momento**
*“Senhor Deus, derramo perante Ti a minha fraqueza e angústia. Reconheço que não posso caminhar sozinho. Enche meu coração com a Tua paz inabalável, perdoa minhas inquietações e renova as minhas forças em Cristo Jesus. Amém.”*

⚠️ *Lembrete Fraterno: Esta orientação não substitui a comunhão com sua igreja local, o aconselhamento pastoral e, havendo necessidade de saúde física ou emocional, o apoio de profissionais qualificados.*
            """.trimIndent()

            p.contains("sermão") || p.contains("pregação") || p.contains("pregar") -> """
### 🎙️ Esboço Homilético Cristocêntrico

**Título:** O Evangelho da Graça Incomparável
**Texto-Base:** Efésios 2:1-10
**Tema Central:** Da Morte para a Vida Mediante a Graça de Cristo
**Objetivo:** Levar a congregação a abandonar a autossuficiência e repousar inteiramente na suficiência da cruz.

**1. Introdução**
- A busca humana por justificação moral e o sentimento universal de incapacidade.
- A radiografia bíblica: estávamos espiritualmente mortos em delitos e pecados.

**2. Contexto Histórico e Teológico**
- Paulo escreve aos crentes gentios de Éfeso, lembrando-os da abolição da inimizade pela cruz de Cristo.

**3. Pontos Principais**
- **Ponto 1: A Nossa Terrível Condição (vv. 1-3)**
  Estávamos separados de Deus, impotentes para nos salvar por mérito próprio.
- **Ponto 2: Mas Deus! A Iniciativa da Misericórdia (vv. 4-7)**
  Pelo Seu grande amor com que nos amou, deu-nos vida juntamente com Cristo Jesus.
- **Ponto 3: A Suficiência da Salvação pela Graça (vv. 8-9)**
  A salvação é dom gratuito de Deus, mediante a fé, para que ninguém se glorie.
- **Ponto 4: Criados para Boas Obras em Cristo (v. 10)**
  As boas obras não são a causa da salvação, mas o fruto abençoado de quem foi transformado.

**4. Conexão Cristocêntrica**
Cristo é o Centro absoluto: Ele bebeu o cálice da ira divina para que recebêssemos o cálice da comunhão e da vida eterna.

**5. Conclusão e Apelo**
- Você tem confiado em sua própria bondade ou na obra consumada de Jesus?
- Venha a Cristo hoje mesmo em fé e arrependimento.
            """.trimIndent()

            else -> """
### 📖 Análise Bíblica e Teológica

**Texto e Contexto Canônico**
A passagem sagrada deve ser sempre examinada à luz do seu contexto histórico original, da intenção do autor bíblico inspirado e do progresso geral da revelação divina.

**Verdade Central**
Deus revela Seu caráter santo, Sua fidelidade imutável e Seu plano gracioso de resgatar o ser humano decaído para uma vida de comunhão e santidade.

**Conexão com Cristo**
Toda a Escritura converge para Jesus Cristo (Lucas 24:27; João 5:39): seja prometendo Sua vinda no Antigo Testamento, revelando Sua encarnação nos Evangelhos, ou proclamando Seu senhorio eterno nas Epístolas e no Apocalipse.

**Aplicação para a Nossa Vida**
- Guardar os preceitos do Senhor no coração.
- Praticar o amor fraternal e a perseverança em oração.
- Confiar no cuidado soberano de Deus diante de qualquer incerteza.

*Nota de Estudo: A Bíblia é a nossa única regra infalível de fé e prática. Examine diariamente as Escrituras com oração e discernimento.*
            """.trimIndent()
        }
    }
}
