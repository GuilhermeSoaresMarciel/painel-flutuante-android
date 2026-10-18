# GGDITOFC – Painel Flutuante Android (Java + C++ / JNI)

Template de painel flutuante (overlay) para Android. A interface roda em Java/Kotlin e a lista de funções e o tratamento dos botões ficam em uma biblioteca nativa em C++, ligada pelo JNI.

## Funcionalidades

- Janela flutuante sobre outros apps (`TYPE_APPLICATION_OVERLAY`)
- Menu arrastável e recolhível: um toque simples abre ou fecha, e arrastar move
- Botões ON/OFF criados dinamicamente a partir da lista de funções definida no C++
- Callback nativo `Preferences_Changes` chamado a cada clique, com o número da função, o nome, o valor inteiro, o booleano e uma string
- Tela inicial com um botão para abrir o painel e outro para abrir o canal do YouTube

## Estrutura do projeto

```
app/src/main/
├── java/com/ggditpanel/
│   ├── MainActivity.kt        # Tela inicial, pede a permissão de overlay e inicia o serviço
│   └── FloatingService.java   # Serviço do overlay, cria os botões e faz a ponte com o JNI
├── cpp/
│   ├── native-lib.cpp         # getFeatureList() e Preferences_Changes()
│   └── CMakeLists.txt
├── res/                       # Layouts, cores e tema
└── AndroidManifest.xml
```

## Como funciona

1. `MainActivity` verifica `Settings.canDrawOverlays()`. Sem a permissão, abre a tela de configuração do sistema. Com ela, inicia o `FloatingService`.
2. `FloatingService` carrega a `native-lib` e chama `getFeatureList()`, que devolve as funções no formato `ID_Tipo_Nome` (exemplo: `1_ButtonOnOff_EXAMPLE`).
3. Para cada item, o serviço cria um botão ON/OFF.
4. Ao clicar, o serviço chama `handleFeature()` no Java e `Preferences_Changes()` no C++.

## Como adicionar uma função

No `native-lib.cpp`, inclua a função na lista:

```cpp
const char* features[] = {
    "1_ButtonOnOff_EXAMPLE",
    "2_ButtonOnOff_Minha Função"
};
```

Depois trate o clique no `switch` de `Preferences_Changes` (C++) e/ou em `handleFeature` (Java).

## Requisitos

- Android Studio com SDK 36 e NDK/CMake instalados
- minSdk 26 (Android 8.0)
- Permissão "Exibir sobre outros apps" (`SYSTEM_ALERT_WINDOW`)

## Build

```bash
./gradlew assembleDebug
```

O APK sai em `app/build/outputs/apk/debug/`.

## Aviso

Projeto com fins educacionais, para estudo de JNI, serviços e overlays no Android. Use apenas em aplicativos próprios ou com autorização.

## Autor

# Guilherme Soares Marciel
