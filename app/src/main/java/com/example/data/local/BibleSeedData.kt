package com.example.data.local

import com.example.data.model.BibleBook
import com.example.data.model.BibleVerse
import com.example.data.model.BibleVersion
import com.example.data.model.BiblicalCharacter
import com.example.data.model.CrossReference
import com.example.data.model.Devotional
import com.example.data.model.Favorite
import com.example.data.model.Note
import com.example.data.model.PrayerRequest
import com.example.data.model.StudyTopic

object BibleSeedData {

    val versions = listOf(
        BibleVersion(
            id = "ARC",
            name = "Almeida Revista e Corrigida",
            abbreviation = "ARC",
            copyrightInfo = "Domínio Público — Tradução histórica de João Ferreira de Almeida.",
            isPublicDomain = true,
            isDefault = true
        ),
        BibleVersion(
            id = "BLIVRE",
            name = "Bíblia Livre (BLIVRE)",
            abbreviation = "BLIVRE",
            copyrightInfo = "Licença Creative Commons (CC-BY-SA 3.0) — Tradução moderna livre.",
            isPublicDomain = false,
            isDefault = false
        ),
        BibleVersion(
            id = "BVE",
            name = "Bíblia Viva Estudo (Canônica)",
            abbreviation = "BVE",
            copyrightInfo = "Edição com aparato textual canônico cristocêntrico.",
            isPublicDomain = true,
            isDefault = false
        )
    )

    val books = listOf(
        // Antigo Testamento - Pentateuco
        BibleBook("GEN", 1, "Gênesis", "Gn", "AT", "Pentateuco", 50, "Moisés", "Origens do universo, queda humana e aliança patriarcal.", "Cristo é o Descendente da Mulher que esmaga a serpente (Gn 3:15) e a semente de Abraão."),
        BibleBook("EXO", 2, "Êxodo", "Êx", "AT", "Pentateuco", 40, "Moisés", "Libertação do Egito, entrega da Lei no Sinai e Tabernáculo.", "Cristo é o Cordeiro Pascal e o verdadeiro Tabernáculo que habita entre nós."),
        BibleBook("LEV", 3, "Levítico", "Lv", "AT", "Pentateuco", 27, "Moisés", "Santidade de Deus, sacrifícios e sacerdócio levítico.", "Cristo é o Sumo Sacerdote perfeito e o Sacrifício definitivo que expia nossos pecados."),
        BibleBook("NUM", 4, "Números", "Nm", "AT", "Pentateuco", 36, "Moisés", "Peregrinação de Israel no deserto por 40 anos.", "Cristo é a Rocha ferida da qual flui água viva e a Serpente de bronze levantada na cruz."),
        BibleBook("DEU", 5, "Deuteronômio", "Dt", "AT", "Pentateuco", 34, "Moisés", "Discursos de despedida de Moisés renovando a aliança.", "Cristo é o Profeta maior que Moisés que nos traz a nova e definitiva aliança."),

        // Históricos
        BibleBook("JOS", 6, "Josué", "Js", "AT", "Históricos", 24, "Josué", "Conquista e divisão da Terra Prometida.", "Cristo é o Capitão dos exércitos do Senhor e o verdadeiro descanso para o povo."),
        BibleBook("JDG", 7, "Juízes", "Jz", "AT", "Históricos", 21, "Samuel (tradicional)", "Ciclos de apostasia, opressão e libertação por juízes.", "Cristo é o Libertador definitivo que quebra as correntes do pecado para sempre."),
        BibleBook("RUT", 8, "Rute", "Rt", "AT", "Históricos", 4, "Desconhecido", "Fidelidade e redenção na vida familiar de Noemi e Rute.", "Cristo é nosso Parente Remidor (Goel) que acolhe os gentios na aliança."),
        BibleBook("1SA", 9, "1 Samuel", "1Sm", "AT", "Históricos", 31, "Samuel / Natã / Gade", "Transição de teocracia para monarquia com Saul e Davi.", "Cristo é o Ungido do Senhor, o verdadeiro Rei do Reino eterno."),
        BibleBook("2SA", 10, "2 Samuel", "2Sm", "AT", "Históricos", 24, "Natã / Gade", "O reinado de Davi e a promessa da dinastia perpétua.", "Cristo é o Filho de Davi cujo trono não terá fim."),
        BibleBook("1KI", 11, "1 Reis", "1Rs", "AT", "Históricos", 22, "Desconhecido / Jeremias", "Glória do reino de Salomão e cisão de Israel e Judá.", "Cristo é mais sábio que Salomão e o verdadeiro Templo."),
        BibleBook("2KI", 12, "2 Reis", "2Rs", "AT", "Históricos", 25, "Desconhecido / Jeremias", "Declínio dos reinos e cativeiros assírio e babilônico.", "Cristo é o Rei fiel que restaura o remanescente prometido."),
        BibleBook("1CH", 13, "1 Crônicas", "1Cr", "AT", "Históricos", 29, "Esdras", "Genealogias sacerdotais e preparativos de Davi para o Templo.", "Cristo é a glória do sacerdócio e o Rei prometido."),
        BibleBook("2CH", 14, "2 Crônicas", "2Cr", "AT", "Históricos", 36, "Esdras", "História de Judá com ênfase na adoração e arrependimento.", "Cristo é a resposta à oração de restauração e cura da terra."),
        BibleBook("EZR", 15, "Esdras", "Ed", "AT", "Históricos", 10, "Esdras", "Retorno do exílio babilônico e reconstrução do Templo.", "Cristo é o Restaurador da adoração pura e viva."),
        BibleBook("NEH", 16, "Neemias", "Ne", "AT", "Históricos", 13, "Neemias", "Reconstrução dos muros de Jerusalém e reformas comunitárias.", "Cristo é o Reconstrutor das brechas de nossas vidas e o Protetor da Igreja."),
        BibleBook("EST", 17, "Ester", "Et", "AT", "Históricos", 10, "Mardoqueu (tradicional)", "Providência invisível de Deus preservando o povo judeu.", "Cristo é o nosso Advogado diante do Pai que intercede por nosso resgate."),

        // Poéticos
        BibleBook("JOB", 18, "Jó", "Jó", "AT", "Poéticos", 42, "Desconhecido", "O mistério do sofrimento do justo e a soberania divina.", "Cristo é o Redentor que vive e que no fim se levantará sobre a terra."),
        BibleBook("PSA", 19, "Salmos", "Sl", "AT", "Poéticos", 150, "Davi, Asafe e outros", "Coletânea de orações, louvores, lamentos e messianismo.", "Cristo é o Bom Pastor (Sl 23) e o Rei Ungido que ressuscita (Sl 16 e 22)."),
        BibleBook("PRO", 20, "Provérbios", "Pv", "AT", "Poéticos", 31, "Salomão e sábios", "Sabedoria prática para a vida cotidiana com temor a Deus.", "Cristo é a Sabedoria de Deus encarnada (1Co 1:24)."),
        BibleBook("ECC", 21, "Eclesiastes", "Ec", "AT", "Poéticos", 12, "Salomão", "A busca pelo sentido da existência debaixo do sol.", "Cristo dá pleno sentido à vida e esperança para além da sepultura."),
        BibleBook("SNG", 22, "Cantares", "Ct", "AT", "Poéticos", 8, "Salomão", "Celebração do amor conjugal puro e apaixonado.", "Aponta para o amor sacrificial e eterno de Cristo por Sua noiva, a Igreja."),

        // Profetas Maiores
        BibleBook("ISA", 23, "Isaías", "Is", "AT", "Profetas Maiores", 66, "Isaías", "Visões de juízo, graça e a redenção de Sião.", "Cristo é o Servo Sofredor (Is 53), Emanuel (Is 7:14) e Príncipe da Paz."),
        BibleBook("JER", 24, "Jeremias", "Jr", "AT", "Profetas Maiores", 52, "Jeremias", "O profeta chorão alertando sobre a queda de Jerusalém.", "Cristo é o Mediador da Nova Aliança gravada no coração (Jr 31:31)."),
        BibleBook("LAM", 25, "Lamentações", "Lm", "AT", "Profetas Maiores", 5, "Jeremias", "Pranto fúnebre pela destruição de Jerusalém e fidelidade divina.", "Cristo é o Homem de dores que experimentou o desamparo em nosso favor."),
        BibleBook("EZK", 26, "Ezequiel", "Ez", "AT", "Profetas Maiores", 48, "Ezequiel", "Glória do Senhor, coração de carne e o novo templo espiritual.", "Cristo é o Bom Pastor de Ez 34 e a fonte das águas purificadoras."),
        BibleBook("DAN", 27, "Daniel", "Dn", "AT", "Profetas Maiores", 12, "Daniel", "Fidelidade no cativeiro e impérios mundiais sob o domínio divino.", "Cristo é a Pedra que destrói os reinos humanos e o Filho do Homem celestial."),

        // Profetas Menores
        BibleBook("HOS", 28, "Oséias", "Os", "AT", "Profetas Menores", 14, "Oséias", "O amor leal de Deus em contraste com a infidelidade de Israel.", "Cristo é o Esposo compassivo que redime a noiva infiel por Sua graça."),
        BibleBook("JOL", 29, "Joel", "Jl", "AT", "Profetas Menores", 3, "Joel", "Praga de gafanhotos e promessa do derramamento do Espírito.", "Cristo é Aquele que derrama o Espírito Santo sobre toda a carne."),
        BibleBook("AMO", 30, "Amós", "Am", "AT", "Profetas Menores", 9, "Amós", "Justiça social e juízo divino contra a religiosidade vazia.", "Cristo restaura a tenda caída de Davi para acolher todos os povos."),
        BibleBook("OBA", 31, "Obadias", "Ob", "AT", "Profetas Menores", 1, "Obadias", "Juízo contra o orgulho de Edom e triunfo de Sião.", "Cristo estabelece o Reino inabalável do Senhor."),
        BibleBook("JON", 32, "Jonas", "Jn", "AT", "Profetas Menores", 4, "Jonas", "A fuga do profeta e a misericórdia de Deus alcançando Nínive.", "Cristo esteve 3 dias no ventre da terra como sinal de ressurreição."),
        BibleBook("MIC", 33, "Miquéias", "Mq", "AT", "Profetas Menores", 7, "Miquéias", "Acusação profética e promessa do Governador nascido em Belém.", "Cristo é o Governador eterno nascido em Belém Efrata (Mq 5:2)."),
        BibleBook("NAM", 34, "Naum", "Na", "AT", "Profetas Menores", 3, "Naum", "A queda definitiva da opressora Nínive.", "Cristo é o refúgio seguro no dia da angústia."),
        BibleBook("HAB", 35, "Habacuque", "Hc", "AT", "Profetas Menores", 3, "Habacuque", "Diálogo com Deus sobre o mal e cântico de confiança.", "Cristo é a esperança da qual provém a certeza: o justo viverá pela fé."),
        BibleBook("ZEP", 36, "Sofonias", "Sf", "AT", "Profetas Menores", 3, "Sofonias", "O Grande Dia do Senhor e o regozijo divino sobre os redimidos.", "Cristo é o Senhor que se cala em Seu amor e exulta sobre nós com cânticos."),
        BibleBook("HAG", 37, "Ageu", "Ag", "AT", "Profetas Menores", 2, "Ageu", "Exortação a priorizar a reconstrução da Casa de Deus.", "Cristo é o Desejado de todas as nações e a maior glória da Casa."),
        BibleBook("ZEC", 38, "Zacarias", "Zc", "AT", "Profetas Menores", 14, "Zacarias", "Visões messiânicas vívidas sobre o Rei humilde.", "Cristo é o Rei humilde montado num jumentinho e o Pastor ferido."),
        BibleBook("MAL", 39, "Malaquias", "Ml", "AT", "Profetas Menores", 4, "Malaquias", "Último chamado ao arrependimento antes dos 400 anos de silêncio.", "Cristo é o Sol da Justiça que nasce trazendo cura em Suas asas."),

        // Novo Testamento - Evangelhos
        BibleBook("MAT", 40, "Mateus", "Mt", "NT", "Evangelhos", 28, "Mateus (Levi)", "Jesus como o Messias prometido, Rei de Israel e cumprimento do AT.", "Cristo é o Rei Messias e o novo Moisés que cumpre toda a Lei."),
        BibleBook("MRK", 41, "Marcos", "Mc", "NT", "Evangelhos", 16, "João Marcos", "Jesus como o Servo de Deus que age com poder e dá a Sua vida.", "Cristo é o Servo Sofredor que veio servir e dar Sua vida em resgate."),
        BibleBook("LUK", 42, "Lucas", "Lc", "NT", "Evangelhos", 24, "Lucas", "Jesus como o Filho do Homem compassivo com pecadores e marginalizados.", "Cristo é o Salvador que busca e salva o que estava perdido."),
        BibleBook("JHN", 43, "João", "Jo", "NT", "Evangelhos", 21, "Apóstolo João", "Jesus como o Filho Eterno de Deus que concede vida eterna.", "Cristo é o Verbo Eterno encarnado, a Luz do Mundo e a Ressurreição."),

        // Histórico NT
        BibleBook("ACT", 44, "Atos", "At", "NT", "Histórico", 28, "Lucas", "O derramamento do Espírito e expansão da Igreja apostólica.", "Cristo ressurreto governa e expande Seu Reino através da Igreja pelo Espírito."),

        // Cartas Paulinas
        BibleBook("ROM", 45, "Romanos", "Rm", "NT", "Cartas de Paulo", 16, "Apóstolo Paulo", "A teologia magna da justificação pela fé somente em Cristo.", "Cristo é a nossa Justiça, Justificação, Redenção e Paz com Deus."),
        BibleBook("1CO", 46, "1 Coríntios", "1Co", "NT", "Cartas de Paulo", 16, "Apóstolo Paulo", "Resolução de divisões, pureza, ceia, dons e a ressurreição.", "Cristo é o Poder e a Sabedoria de Deus, e as Primícias da ressurreição."),
        BibleBook("2CO", 47, "2 Coríntios", "2Co", "NT", "Cartas de Paulo", 13, "Apóstolo Paulo", "Defesa apostólica e a força de Deus aperfeiçoada na fraqueza.", "Cristo é Aquele que se fez pobre para que fôssemos enriquecidos."),
        BibleBook("GAL", 48, "Gálatas", "Gl", "NT", "Cartas de Paulo", 6, "Apóstolo Paulo", "A liberdade do Evangelho contra o legalismo circuncisionista.", "Cristo nos resgatou da maldição da Lei, fazendo-se maldição por nós."),
        BibleBook("EPH", 49, "Efésios", "Ef", "NT", "Cartas de Paulo", 6, "Apóstolo Paulo", "O plano eterno de Deus em unir todas as coisas em Cristo.", "Cristo é o Cabeça supremo da Igreja, que é o Seu corpo."),
        BibleBook("PHP", 50, "Filipenses", "Fp", "NT", "Cartas de Paulo", 4, "Apóstolo Paulo", "A alegria inabalável em meio às tribulações e humilhação de Cristo.", "Cristo é o exemplo supremo de humilhação e exaltação gloriosa."),
        BibleBook("COL", 51, "Colossenses", "Cl", "NT", "Cartas de Paulo", 4, "Apóstolo Paulo", "A supremacia absoluta de Cristo sobre toda a criação cósmica.", "Cristo é a Imagem do Deus invisível e a Plenitude da divindade corporalmente."),
        BibleBook("1TH", 52, "1 Tessalonicenses", "1Ts", "NT", "Cartas de Paulo", 5, "Apóstolo Paulo", "Esperança escatológica e a bendita volta de Cristo.", "Cristo é o Senhor que descerá dos céus com alarido e nos arrebatará."),
        BibleBook("2TH", 53, "2 Tessalonicenses", "2Ts", "NT", "Cartas de Paulo", 3, "Apóstolo Paulo", "Perseverança contra o homem da iniquidade e consolo da glória.", "Cristo é o Juiz glorioso que derrotará todo engano pelo sopro de Sua boca."),
        BibleBook("1TI", 54, "1 Timóteo", "1Tm", "NT", "Cartas de Paulo", 6, "Apóstolo Paulo", "Instruções pastorais sobre liderança piedosa e ordem na Igreja.", "Cristo é o único Mediador entre Deus e os homens."),
        BibleBook("2TI", 55, "2 Timóteo", "2Tm", "NT", "Cartas de Paulo", 4, "Apóstolo Paulo", "O último testamento de Paulo: combate o bom combate na fé.", "Cristo é o justo Juiz que concederá a coroa da justiça."),
        BibleBook("TIT", 56, "Tito", "Tt", "NT", "Cartas de Paulo", 3, "Apóstolo Paulo", "Organização da igreja em Creta e boas obras geradas pela graça.", "Cristo é o nosso grande Deus e Salvador que nos purificou."),
        BibleBook("PHM", 57, "Filemom", "Fm", "NT", "Cartas de Paulo", 1, "Apóstolo Paulo", "Reconciliação prática entre mestre e escravo em Cristo.", "Cristo é Aquele que quita as nossas dívidas e nos reconcilia como irmãos."),

        // Cartas Gerais
        BibleBook("HEB", 58, "Hebreus", "Hb", "NT", "Cartas Gerais", 13, "Autor desconhecido", "A superioridade absoluta de Cristo sobre anjos, Moisés e sacrifícios.", "Cristo é o Autor e Consumador de nossa fé e nosso Eterno Sumo Sacerdote."),
        BibleBook("JAS", 59, "Tiago", "Tg", "NT", "Cartas Gerais", 5, "Tiago (irmão do Senhor)", "A fé autêntica demonstrada através de obras piedosas.", "Cristo é a fonte de sabedoria celestial e o Senhor da glória."),
        BibleBook("1PE", 60, "1 Pedro", "1Pe", "NT", "Cartas Gerais", 5, "Apóstolo Pedro", "Esperança viva e perseverança no sofrimento dos peregrinos.", "Cristo é a Pedra Angular preciosa e o Bispo das nossas almas."),
        BibleBook("2PE", 61, "2 Pedro", "2Pe", "NT", "Cartas Gerais", 3, "Apóstolo Pedro", "Alerta solene contra falsos mestres e certeza do Dia do Senhor.", "Cristo é o Senhor paciente que deseja que todos cheguem ao arrependimento."),
        BibleBook("1JN", 62, "1 João", "1Jo", "NT", "Cartas Gerais", 5, "Apóstolo João", "Certeza da salvação, comunhão com a Luz e amor fraternal.", "Cristo é a propiciação pelos nossos pecados e o Advogado com o Pai."),
        BibleBook("2JN", 63, "2 João", "2Jo", "NT", "Cartas Gerais", 1, "Apóstolo João", "Andar na verdade e no mandamento do amor fraternal.", "Cristo veio em carne e perseveramos em Sua santa doutrina."),
        BibleBook("3JN", 64, "3 João", "3Jo", "NT", "Cartas Gerais", 1, "Apóstolo João", "Hospitalidade com os obreiros da verdade versus arrogância de Diótrefes.", "Cristo nos chama a ser cooperadores da verdade por amor ao Seu Nome."),
        BibleBook("JUD", 65, "Judas", "Jd", "NT", "Cartas Gerais", 1, "Judas", "Batalhar pela fé que uma vez foi entregue aos santos.", "Cristo é poderoso para nos guardar de tropeçar e nos apresentar irrepreensíveis."),

        // Profético NT
        BibleBook("REV", 66, "Apocalipse", "Ap", "NT", "Profético", 22, "Apóstolo João", "A revelação final da vitória de Deus e do Cordeiro sobre o mal.", "Cristo é o Alfa e o Ômega, o Cordeiro no Trono, o Leão de Judá e o Rei dos reis.")
    )

    val keyVerses = listOf(
        // João 3
        BibleVerse(0, "ARC", "JHN", "João", 3, 1, "E havia entre os fariseus um homem, chamado Nicodemos, príncipe dos judeus."),
        BibleVerse(0, "ARC", "JHN", "João", 3, 2, "Este foi ter de noite com Jesus, e disse-lhe: Rabi, bem sabemos que és Mestre, vindo de Deus; porque ninguém pode fazer estes sinais que tu fazes, se Deus não for com ele."),
        BibleVerse(0, "ARC", "JHN", "João", 3, 3, "Jesus respondeu, e disse-lhe: Na verdade, na verdade te digo que aquele que não nascer de novo, não pode ver o reino de Deus."),
        BibleVerse(0, "ARC", "JHN", "João", 3, 14, "E, como Moisés levantou a serpente no deserto, assim importa que o Filho do homem seja levantado;"),
        BibleVerse(0, "ARC", "JHN", "João", 3, 15, "Para que todo aquele que nele crê não pereça, mas tenha a vida eterna."),
        BibleVerse(0, "ARC", "JHN", "João", 3, 16, "Porque Deus amou o mundo de tal maneira que deu o seu Filho unigênito, para que todo aquele que nele crê não pereça, mas tenha a vida eterna."),
        BibleVerse(0, "ARC", "JHN", "João", 3, 17, "Porque Deus enviou o seu Filho ao mundo, não para que condenasse o mundo, mas para que o mundo fosse salvo por ele."),
        BibleVerse(0, "ARC", "JHN", "João", 3, 18, "Quem crê nele não é condenado; mas quem não crê já está condenado, porquanto não crê no nome do unigênito Filho de Deus."),

        // João 1
        BibleVerse(0, "ARC", "JHN", "João", 1, 1, "No princípio era o Verbo, e o Verbo estava com Deus, e o Verbo era Deus."),
        BibleVerse(0, "ARC", "JHN", "João", 1, 2, "Ele estava no princípio com Deus."),
        BibleVerse(0, "ARC", "JHN", "João", 1, 3, "Todas as coisas foram feitas por ele, e sem ele nada do que foi feito se fez."),
        BibleVerse(0, "ARC", "JHN", "João", 1, 4, "Nele estava a vida, e a vida era a luz dos homens."),
        BibleVerse(0, "ARC", "JHN", "João", 1, 14, "E o Verbo se fez carne, e habitou entre nós, e vimos a sua glória, como a glória do unigênito do Pai, cheio de graça e de verdade."),
        BibleVerse(0, "ARC", "JHN", "João", 1, 29, "No dia seguinte João viu a Jesus, que vinha para ele, e disse: Eis o Cordeiro de Deus, que tira o pecado do mundo."),

        // Romanos 8
        BibleVerse(0, "ARC", "ROM", "Romanos", 8, 1, "Portanto, agora nenhuma condenação há para os que estão em Cristo Jesus, que não andam segundo a carne, mas segundo o Espírito."),
        BibleVerse(0, "ARC", "ROM", "Romanos", 8, 28, "E sabemos que todas as coisas contribuem juntamente para o bem daqueles que amam a Deus, daqueles que são chamados segundo o seu propósito."),
        BibleVerse(0, "ARC", "ROM", "Romanos", 8, 31, "Que diremos, pois, a estas coisas? Se Deus é por nós, quem será contra nós?"),
        BibleVerse(0, "ARC", "ROM", "Romanos", 8, 32, "Aquele que nem mesmo a seu próprio Filho poupou, antes o entregou por todos nós, como nos não dará também com ele todas as coisas?"),
        BibleVerse(0, "ARC", "ROM", "Romanos", 8, 38, "Porque estou certo de que, nem a morte, nem a vida, nem os anjos, nem os principados, nem as potestades, nem o presente, nem o porvir,"),
        BibleVerse(0, "ARC", "ROM", "Romanos", 8, 39, "Nem a altura, nem a profundidade, nem alguma outra criatura nos poderá separar do amor de Deus, que está em Cristo Jesus nosso Senhor."),

        // Salmo 23
        BibleVerse(0, "ARC", "PSA", "Salmos", 23, 1, "O Senhor é o meu pastor, nada me faltará."),
        BibleVerse(0, "ARC", "PSA", "Salmos", 23, 2, "Deitar-me faz em verdes pastos, guia-me mansamente a águas tranquilas."),
        BibleVerse(0, "ARC", "PSA", "Salmos", 23, 3, "Refrigera a minha alma; guia-me pelas veredas da justiça, por amor do seu nome."),
        BibleVerse(0, "ARC", "PSA", "Salmos", 23, 4, "Ainda que eu andasse pelo vale da sombra da morte, não temeria mal algum, porque tu estás comigo; a tua vara e o teu cajado me consolam."),
        BibleVerse(0, "ARC", "PSA", "Salmos", 23, 5, "Preparas uma mesa perante mim na presença dos meus inimigos, unges a minha cabeça com óleo, o meu cálice transborda."),
        BibleVerse(0, "ARC", "PSA", "Salmos", 23, 6, "Certamente que a bondade e a misericórdia me seguirão todos os dias da minha vida; e habitarei na casa do Senhor por longos dias."),

        // Salmo 91
        BibleVerse(0, "ARC", "PSA", "Salmos", 91, 1, "Aquele que habita no esconderijo do Altíssimo, à sombra do Onipotente descansará."),
        BibleVerse(0, "ARC", "PSA", "Salmos", 91, 2, "Direi do Senhor: Ele é o meu Deus, o meu refúgio, a minha fortaleza, e nele confiarei."),
        BibleVerse(0, "ARC", "PSA", "Salmos", 91, 3, "Porque ele te livrará do laço do passarinheiro, e da peste perniciosa."),
        BibleVerse(0, "ARC", "PSA", "Salmos", 91, 4, "Ele te cobrirá com as suas penas, e debaixo das suas asas te confiarás; a sua verdade será o teu escudo e broquel."),

        // Isaías 53
        BibleVerse(0, "ARC", "ISA", "Isaías", 53, 3, "Era desprezado, e o mais rejeitado entre os homens, homem de dores, e experimentado nos trabalhos; e, como um de quem os homens escondiam o rosto, era desprezado, e não fizemos dele caso algum."),
        BibleVerse(0, "ARC", "ISA", "Isaías", 53, 4, "Verdadeiramente ele tomou sobre si as nossas enfermidades, e as nossas dores levou sobre si; e nós o reputávamos por aflito, ferido de Deus, e oprimido."),
        BibleVerse(0, "ARC", "ISA", "Isaías", 53, 5, "Mas ele foi ferido por causa das nossas transgressões, e moído por causa das nossas iniquidades; o castigo que nos traz a paz estava sobre ele, e pelas suas pisaduras fomos sarados."),
        BibleVerse(0, "ARC", "ISA", "Isaías", 53, 6, "Todos nós andávamos desgarrados como ovelhas; cada um se desviava pelo seu caminho; mas o Senhor fez cair sobre ele a iniquidade de nós todos."),

        // Efésios 2
        BibleVerse(0, "ARC", "EPH", "Efésios", 2, 8, "Porque pela graça sois salvos, por meio da fé; e isto não vem de vós, é dom de Deus."),
        BibleVerse(0, "ARC", "EPH", "Efésios", 2, 9, "Não vem das obras, para que ninguém se glorie;"),
        BibleVerse(0, "ARC", "EPH", "Efésios", 2, 10, "Porque somos feitura sua, criados em Cristo Jesus para as boas obras, as quais Deus preparou para que andássemos nelas."),

        // Filipenses 2
        BibleVerse(0, "ARC", "PHP", "Filipenses", 2, 5, "De sorte que haja em vós o mesmo sentimento que houve também em Cristo Jesus,"),
        BibleVerse(0, "ARC", "PHP", "Filipenses", 2, 6, "Que, sendo em forma de Deus, não teve por usurpação ser igual a Deus,"),
        BibleVerse(0, "ARC", "PHP", "Filipenses", 2, 7, "Mas esvaziou-se a si mesmo, tomando a forma de servo, fazendo-se semelhante aos homens;"),
        BibleVerse(0, "ARC", "PHP", "Filipenses", 2, 8, "E, achado na forma de homem, humilhou-se a si mesmo, sendo obediente até à morte, e morte de cruz."),
        BibleVerse(0, "ARC", "PHP", "Filipenses", 2, 9, "Por isso, também Deus o exaltou soberanamente, e lhe deu um nome que é sobre todo o nome;"),
        BibleVerse(0, "ARC", "PHP", "Filipenses", 2, 10, "Para que ao nome de Jesus se dobre todo o joelho dos que estão nos céus, e na terra, e debaixo da terra,"),
        BibleVerse(0, "ARC", "PHP", "Filipenses", 2, 11, "E toda a língua confesse que Jesus Cristo é o Senhor, para glória de Deus Pai."),

        // Gênesis 1
        BibleVerse(0, "ARC", "GEN", "Gênesis", 1, 1, "No princípio criou Deus os céus e a terra."),
        BibleVerse(0, "ARC", "GEN", "Gênesis", 1, 2, "E a terra era sem forma e vazia; e havia trevas sobre a face do abismo; e o Espírito de Deus se movia sobre a face das águas."),
        BibleVerse(0, "ARC", "GEN", "Gênesis", 1, 3, "E disse Deus: Haja luz; e houve luz."),
        BibleVerse(0, "ARC", "GEN", "Gênesis", 1, 26, "E disse Deus: Façamos o homem à nossa imagem, conforme a nossa semelhança; e domine sobre os peixes do mar, e sobre as aves dos céus, e sobre o gado, e sobre toda a terra, e sobre todo o réptil que se move sobre a terra."),
        BibleVerse(0, "ARC", "GEN", "Gênesis", 1, 27, "E criou Deus o homem à sua imagem; à imagem de Deus o criou; homem e mulher os criou.")
    )

    val crossReferences = listOf(
        CrossReference(0, "JHN", 3, 16, "Romanos", 5, 8, "Cristocêntrico", "Deus prova o seu amor para conosco, em que Cristo morreu por nós, sendo nós ainda pecadores."),
        CrossReference(0, "JHN", 3, 16, "1 João", 4, 9, "Tema Paralelo", "Nisto se manifestou o amor de Deus para conosco: que Deus enviou seu Filho unigênito ao mundo."),
        CrossReference(0, "JHN", 3, 14, "Números", 21, 9, "Tipologia", "Moisés fez uma serpente de metal e pô-la sobre uma haste; e quando alguém era mordido e olhava para ela, vivia."),
        CrossReference(0, "ISA", 53, 5, "1 Pedro", 2, 24, "Cumprimento", "Levando ele mesmo em seu corpo os nossos pecados sobre o madeiro, para que, mortos para os pecados, pudéssemos viver para a justiça."),
        CrossReference(0, "PSA", 23, 1, "João", 10, 11, "Cristocêntrico", "Eu sou o bom pastor; o bom pastor dá a sua vida pelas ovelhas."),
        CrossReference(0, "EPH", 2, 8, "Romanos", 3, 24, "Doutrina", "Sendo justificados gratuitamente pela sua graça, pela redenção que há em Cristo Jesus.")
    )

    val studyTopics = listOf(
        StudyTopic(
            id = "fe",
            title = "A Natureza da Fé Salvadora",
            definition = "A fé bíblica não é mero assentimento intelectual ou otimismo humano, mas a firme confiança depositada na pessoa, obra e promessa de Cristo Jesus.",
            mainScriptures = "Hebreus 11:1-6; Romanos 10:17; Efésios 2:8-9; Gálatas 2:20",
            historicalContext = "No primeiro século, a fé em Cristo rompeu tanto com a justificação pelas obras da lei mosaica quanto com o politeísmo idólatra romano.",
            relatedCharacters = "Abraão, o pai da fé; Pedro; a mulher cananeia.",
            christConnection = "Cristo é tanto o objeto quanto o Autor e Consumador da nossa fé (Hb 12:2). É a justiça de Cristo recebida pela fé que nos reconcilia com o Pai.",
            practicalApplications = "Viver pela fé nos liberta da ansiedade com o futuro e do desespero nas aflições cotidianas.",
            studyQuestions = "Qual a diferença entre fé salvadora e mera crença teórica? Como a fé cresce na caminhada cristã?"
        ),
        StudyTopic(
            id = "graca",
            title = "A Doutrina da Graça Soberana",
            definition = "O favor imerecido de Deus manifestado aos pecadores culpados, concedendo vida, perdão e justificação totalmente sem mérito humano.",
            mainScriptures = "Efésios 2:1-10; Tito 2:11-14; Romanos 5:1-2; 2 Coríntios 12:9",
            historicalContext = "A Reforma Protestante resgatou o princípio 'Sola Gratia', reafirmando que o homem não compra nem colabora com méritos para a sua salvação.",
            relatedCharacters = "Paulo, outrora perseguidor da Igreja e alcançado pela graça soberana.",
            christConnection = "A graça divina personificou-se em Cristo: 'A lei foi dada por Moisés; a graça e a verdade vieram por Jesus Cristo' (Jo 1:17).",
            practicalApplications = "A consciência da graça gera humildade profunda, gratidão e incapacidade de olhar o próximo com desprezo.",
            studyQuestions = "Como a graça nos ensina a renunciar à impiedade (Tt 2:12)? Por que a salvação não pode ser por obras?"
        ),
        StudyTopic(
            id = "oracao",
            title = "O Ministério e Poder da Oração",
            definition = "Comunhão íntima e reverente da criatura com o Criador, baseada na mediação de Cristo e orientada pela Palavra de Deus.",
            mainScriptures = "Mateus 6:5-15; Filipenses 4:6-7; 1 Tessalonicenses 5:17; Tiago 5:16",
            historicalContext = "No judaísmo do segundo templo, orações frequentes eram rituais fixos. Jesus revolucionou ensinando a orar ao 'Aba, Pai'.",
            relatedCharacters = "Ana, Daniel, Jesus nos dias de Sua carne, a igreja primitiva em Atos 4.",
            christConnection = "Cristo é o único Mediador que nos abre o Santo dos Santos (Hb 10:19-22) e vive para interceder por nós à destra do Pai.",
            practicalApplications = "Apresentar a Deus tanto os grandes anseios espirituais quanto as aflições diárias com ações de graças.",
            studyQuestions = "Por que oramos em nome de Jesus? Como alinhar nossas orações com a soberana vontade divina?"
        ),
        StudyTopic(
            id = "sofrimento",
            title = "O Sofrimento e a Glória Futura",
            definition = "A resposta bíblica à dor: Deus soberanamente utiliza as tribulações terrenas para forjar o caráter cristão e apontar para a glória eterna.",
            mainScriptures = "Romanos 8:18-39; 2 Coríntios 4:16-18; 1 Pedro 4:12-19; Jó 42",
            historicalContext = "Os crentes do primeiro século enfrentavam perseguição imperial cruel e perda de bens por amor ao nome de Cristo.",
            relatedCharacters = "Jó, José no Egito, o apóstolo Paulo nas prisões.",
            christConnection = "Jesus é o Servo Sofredor que suportou a cruz por amor, nos garantindo que nosso sofrimento presente não é o ponto final.",
            practicalApplications = "Em meio à dor, não desfalecer: a aflição presente é leve e momentânea comparada ao eterno peso de glória.",
            studyQuestions = "Como a cruz de Cristo nos impede de achar que Deus é indiferente à dor humana?"
        )
    )

    val biblicalCharacters = listOf(
        BiblicalCharacter(
            id = "abraao",
            name = "Abraão",
            period = "Patriarcal (~2000 a.C.)",
            biography = "Chamado por Deus em Ur dos Caldeus para deixar sua terra e parentela em direção à terra prometida de Canaã.",
            keyEvents = "Chamado de Deus (Gn 12); Aliança confirmada (Gn 15); Nascimento de Isaque na velhice; O sacrifício de Isaque no monte Moriá (Gn 22).",
            virtues = "Obediência incondicional à voz de Deus, hospitalidade, intercessão por Sodoma, fé inabalável contra a esperança humana.",
            flaws = "Temor que o levou a mentir sobre Sara no Egito e com Abimeleque; precipitação no plano de Hagar.",
            lessons = "Deus cumpre fielmente Suas promessas no tempo certo. O justo vive pela fé e não por vista.",
            relatedVerses = "Gênesis 12:1-3; 15:6; Romanos 4:1-25; Hebreus 11:8-19",
            redemptiveRole = "A promessa dada a Abraão ('em ti serão benditas todas as famílias da terra') apontava diretamente para Cristo (Gl 3:16).",
            application = "Confiar em Deus mesmo quando as circunstâncias naturais parecem contradizer a promessa."
        ),
        BiblicalCharacter(
            id = "moises",
            name = "Moisés",
            period = "Êxodo (~1446 a.C.)",
            biography = "Resgatado das águas do Nilo pela filha de Faraó, criado na corte egípcia e comissionado por Deus na sarça ardente.",
            keyEvents = "As dez pragas do Egito; A primeira Páscoa; A travessia do Mar Vermelho; A promulgação da Lei no Sinai; 40 anos liderando o povo no deserto.",
            virtues = "Mansidão notável, fidelidade à Casa de Deus, intercessão incansável pelo povo rebelde.",
            flaws = "Impulsividade ao matar o egípcio na juventude; ferir a rocha em Meribá por ira em vez de falar com ela.",
            lessons = "Nossa capacitação não vem do preparo humano, mas do envio soberano do Senhor. A santidade de Deus exige reverência.",
            relatedVerses = "Êxodo 3; 14; 20; Números 12:3; 20:1-13; Hebreus 11:24-29",
            redemptiveRole = "Moisés é o tipo de Cristo como Profeta, Mediador e Libertador, antecipando Aquele que nos libertaria da escravidão do pecado.",
            application = "Liderar com mansidão e manter o coração sensível à santidade e à presença manifesta do Altíssimo."
        ),
        BiblicalCharacter(
            id = "davi",
            name = "Davi",
            period = "Monarquia Unida (~1000 a.C.)",
            biography = "Pastor de ovelhas em Belém, ungido rei por Samuel, músico, guerreiro e governante sob o qual o reino de Israel foi unificado.",
            keyEvents = "Vitória sobre o gigante Golias; Perseguição por Saul nos desertos; Conquista de Jerusalém; Aliança Davídica (2Sm 7); Arrependimento do pecado com Bate-Seba (Sl 51).",
            virtues = "Homem segundo o coração de Deus, apaixonado pela adoração, espírito generoso e de quebrantamento sincero.",
            flaws = "Adultério com Bate-Seba e conspiração na morte de Urias; passividade na disciplina de seus filhos.",
            lessons = "Nenhum homem está livre de cair se baixar a guarda, mas a misericórdia restauradora de Deus acolhe o coração contrito.",
            relatedVerses = "1 Samuel 16; 17; 2 Samuel 7; 11; 12; Salmos 23; 51; Atos 13:22",
            redemptiveRole = "Cristo é o Filho de Davi que se assenta eternamente no trono messiânico, o verdadeiro Rei justo.",
            application = "Buscar a Deus com adoração sincera e recorrer rapidamente ao arrependimento quando confrontado pelo pecado."
        ),
        BiblicalCharacter(
            id = "pedro",
            name = "Simão Pedro",
            period = "Apostólico (Século I d.C.)",
            biography = "Pescador da Galileia, chamado por Jesus junto ao Mar da Galileia, tornou-se líder dos doze apóstolos.",
            keyEvents = "Confissão messiânica em Cesareia de Filipe; Andar sobre as águas; Negação no pátio do sumo sacerdote; Restauração pelo Senhor ressurreto à beira do lago; Sermão em Pentecostes.",
            virtues = "Zelo ardente, coragem genuína, amor profundo por Cristo, humildade ao receber repreensão.",
            flaws = "Impulsividade na fala e no corte da orelha de Malco; medo que culminou na negação tripla.",
            lessons = "O fracasso momentâneo nas mãos de Jesus é transformado em capacitação pastoral pelo poder da ressurreição.",
            relatedVerses = "Mateus 16:13-19; 26:69-75; João 21:15-19; Atos 2; 1 e 2 Pedro",
            redemptiveRole = "Testemunha ocular da glória, morte e ressurreição de Cristo, foi comissionado a apascentar o rebanho do Sumo Pastor.",
            application = "Aprender a depender não da nossa bravura carnal, mas da suficiência do poder do Espírito Santo."
        )
    )

    val devotionals = listOf(
        Devotional(
            id = 0,
            dayNumber = 1,
            title = "A Plenitude da Graça Revelada",
            scriptureRef = "João 1:16-17",
            scriptureText = "E todos nós recebemos também da sua plenitude, com graça sobre graça. Porque a lei foi dada por Moisés; a graça e a verdade vieram por Jesus Cristo.",
            theme = "Graça e Redenção",
            reflection = "Nenhum de nós precisa viver de restos ou de insuficiência espiritual. Em Jesus, a fonte é inesgotável. Cada manhã traz uma nova medida de graça para enfrentar as batalhas do dia.",
            practicalApplication = "Ao começar o seu dia, entregue suas fraquezas a Cristo em vez de tentar vencê-las com a própria força.",
            reflectionQuestion = "Em qual área da sua vida você precisa se apoiar mais na graça de Cristo hoje?",
            prayer = "Senhor Jesus, obrigado porque a Tua fonte nunca seca. Recebo hoje a Tua graça sobre graça para caminhar em paz, sabedoria e fidelidade. Amém.",
            complementaryReading = "Romanos 5:1-11"
        ),
        Devotional(
            id = 0,
            dayNumber = 2,
            title = "O Bom Pastor e a Certeza do Cuidado",
            scriptureRef = "Salmos 23:1",
            scriptureText = "O Senhor é o meu pastor, nada me faltará.",
            theme = "Descanso e Provisão",
            reflection = "Davi não escreveu este salmo em um palácio isolado do perigo, mas nos vales e desertos. O pastor não promete a ausência de vales escuros, mas garante Sua presença inseparável.",
            practicalApplication = "Identifique uma preocupação que rouba seu sono e declare conscientemente a soberania do Bom Pastor sobre ela.",
            reflectionQuestion = "Você tem confiado na provisão do Pastor ou tentado controlar tudo sozinho?",
            prayer = "Querido Pai, descanso em Teus braços. Sei que nada de bom me faltará, pois Tu és o meu fiel Pastor. Conduz-me em águas tranquilas. Amém.",
            complementaryReading = "João 10:1-18"
        ),
        Devotional(
            id = 0,
            dayNumber = 3,
            title = "Nenhuma Condenação em Cristo",
            scriptureRef = "Romanos 8:1",
            scriptureText = "Portanto, agora nenhuma condenação há para os que estão em Cristo Jesus.",
            theme = "Liberdade no Evangelho",
            reflection = "A voz do acusador tenta nos prender a erros passados. Mas a sentença da cruz foi definitiva: a dívida foi paga por inteiro por Jesus. Você é aceito no Amado.",
            practicalApplication = "Não permita que a culpa sufocante vença a certeza da justificação que você já possui em Cristo.",
            reflectionQuestion = "Há alguma memória do passado que você precisa silenciar com a verdade do Evangelho?",
            prayer = "Pai de misericórdia, louvo-Te pelo sangue precioso de Teu Filho que cancelou minha condenação. Ensina-me a andar em santidade e santa liberdade. Amém.",
            complementaryReading = "Romanos 8:31-39"
        )
    )

    val initialFavorites = listOf(
        Favorite(
            id = 0,
            bookId = "JHN",
            bookName = "João",
            chapter = 3,
            verse = 16,
            text = "Porque Deus amou o mundo de tal maneira que deu o seu Filho unigênito, para que todo aquele que nele crê não pereça, mas tenha a vida eterna.",
            translation = "ARC",
            tags = "Evangelho, Amor de Deus, Salvação"
        ),
        Favorite(
            id = 0,
            bookId = "ROM",
            bookName = "Romanos",
            chapter = 8,
            verse = 28,
            text = "E sabemos que todas as coisas concorrem para o bem daqueles que amam a Deus, daqueles que são chamados segundo o seu propósito.",
            translation = "ARC",
            tags = "Providência, Confiança, Soberania"
        ),
        Favorite(
            id = 0,
            bookId = "PSA",
            bookName = "Salmos",
            chapter = 23,
            verse = 1,
            text = "O Senhor é o meu pastor, nada me faltará.",
            translation = "ARC",
            tags = "Provisão, Cuidado, Paz"
        )
    )

    val initialNotes = listOf(
        Note(
            id = 0,
            title = "A Plenitude da Graça em João 1",
            content = "Graça sobre graça: a revelação de Deus não termina no Sinai com a Lei de Moisés, mas atinge o ápice na pessoa bendita de Jesus Cristo.",
            bookId = "JHN",
            bookName = "João",
            chapter = 1,
            verse = 16,
            category = "Estudos"
        ),
        Note(
            id = 0,
            title = "O Cuidado Soberano do Pastor",
            content = "No Salmo 23, a presença do cajado e da vara traz consolo na tribulação. Jesus é o Bom Pastor que dá a vida pelas Suas ovelhas.",
            bookId = "PSA",
            bookName = "Salmos",
            chapter = 23,
            verse = 1,
            category = "Devocional"
        )
    )

    val initialPrayers = listOf(
        PrayerRequest(
            id = 0,
            title = "Crescimento na Graça e Sabedoria",
            description = "Buscar a Deus diariamente em oração e na leitura da Palavra com o coração quebrantado e sede de Cristo.",
            category = "Vida espiritual",
            isAnswered = true,
            testimony = "Deus tem renovado a comunhão diária e o apetite pela sã doutrina."
        ),
        PrayerRequest(
            id = 0,
            title = "Edificação e Salvação da Família",
            description = "Que a paz e a luz do Evangelho de Cristo alcancem cada um dos meus familiares e amigos.",
            category = "Família",
            isAnswered = false
        )
    )
}
