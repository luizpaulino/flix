[![coverage](https://raw.githubusercontent.com/luizpaulino/flix/badges/jacoco.svg)](https://github.com/luizpaulino/flix/actions/workflows/build.yml)
## Setup e uso da aplicação

- Requisitos:
    - Docker
    - Maven

- Clonar o projeto e rodar o script flix.sh
- Collection do [arquivo JSON da coleção](./streaming_collection.json).

### Endpoints

- **Flix**
    - **Vídeos**
        - Adiciona informações do vídeo: POST - /api/videos

          **Exemplo de Corpo da Requisição:**
          ```json
          {
              "title": "Interestelar",
              "description": "As reservas naturais da Terra estão chegando ao fim e um grupo de astronautas recebe a missão de verificar possíveis planetas para receberem a população mundial, possibilitando a continuação da espécie",
              "category": "Sci-fi"
          }
          ```

          **Exemplo de Resposta:**
          ```json
          {
              "id": "55441a95-8d6b-4b8b-97ee-bba6e6d1bf94",
              "title": "Interestelar",
              "description": "As reservas naturais da Terra estão chegando ao fim e um grupo de astronautas recebe a missão de verificar possíveis planetas para receberem a população mundial, possibilitando a continuação da espécie",
              "url": null,
              "category": "Sci-fi",
              "publication_date": "29/01/2024"
          }
          ```
        - Atualiza informações do vídeo: PUT - /api/videos/{idVideo}

          **Exemplo de Corpo da Requisição:**
            ```json
            {
                "title": "Interestelar",
                "description": "As reservas naturais da Terra estão chegando ao fim e um grupo de astronautas recebe a missão de verificar possíveis planetas para receberem a população mundial, possibilitando a continuação da espécie",
                "category": "Sci-fi"
            }
            ```

        - Upload do vídeo: POST - /api/videos/{idVideo}/upload
          **Corpo da Requisição:** Multipart

          **Parâmetros da Requisição:**
            - `file`: Arquivo de vídeo a ser enviado

          *Observação: Certifique-se de incluir o arquivo de vídeo no corpo da requisição usando Multipart.*

          **Exemplo de Uso:**
            ```bash
            curl -X POST -F "file=@/caminho/do/seu/arquivo/video.mp4" http://URI_DO_FLIX/api/videos/55441a95-8d6b-4b8b-97ee-bba6e6d1bf94/upload
            ```
        - Busca de vídeos: GET - /api/videos

          Um vídeo só fica disponível para busca após o upload do mesmo.

          **Tipo de Requisição:** GET

          **Parâmetros de Consulta:**
            - `field`: Pode ser `category` ou `title`.
            - `query`: Conteúdo a ser buscado.
            - `page`: Número da página (default: 0).
            - `direction`: Direção da ordenação pela data de publicação (default: `desc`).

          **Exemplo de Uso:**
            ```
            GET /api/videos?field=category&query=action&page=0&direction=desc
            ```

          **Exemplo de Resposta:**
          ```json
          {
              "content": [
                  {
                      "id": "1fb186cd-c47e-41f7-aada-60f53ee21707",
                      "title": "First movie",
                      "description": "First movie to test",
                      "url": "https://postech-streaming-upload.s3.sa-east-1.amazonaws.com/1fb186cd-c47e-41f7-aada-60f53ee21707.mp4",
                      "category": "Sci-fi",
                      "publication_date": "27/01/2024"
                  },
                  {
                      "id": "4b65c3fe-ffb1-4672-9fca-3d61ec1dc62b",
                      "title": "Second movie",
                      "description": "Second movie to test",
                      "url": "https://postech-streaming-upload.s3.sa-east-1.amazonaws.com/4b65c3fe-ffb1-4672-9fca-3d61ec1dc62b.mp4",
                      "category": "Comedy",
                      "publication_date": "27/01/2024"
                  },
                  {
                      "id": "55441a95-8d6b-4b8b-97ee-bba6e6d1bf94",
                      "title": "Third movie",
                      "description": "Third movie to test",
                      "url": "https://postech-streaming-upload.s3.sa-east-1.amazonaws.com/55441a95-8d6b-4b8b-97ee-bba6e6d1bf94.mp4",
                      "category": "Games",
                      "publication_date": "27/01/2024"
                  }
              ],
              "pageable": {
                  "pageNumber": 0,
                  "pageSize": 20,
                  "sort": {
                      "empty": false,
                      "sorted": true,
                      "unsorted": false
                  },
                  "offset": 0,
                  "paged": true,
                  "unpaged": false
              },
              "last": true,
              "totalPages": 1,
              "totalElements": 3,
              "size": 20,
              "number": 0,
              "sort": {
                  "empty": false,
                  "sorted": true,
                  "unsorted": false
              },
              "first": true,
              "numberOfElements": 3,
              "empty": false
          }
          ```

    - **Agregadores**
        - Consulta de estatísticas dos vídeos cadastrados: GET - /api/aggregators

      **Exemplo de Resposta:**
      ```json
      {
          "total_videos": 3,
          "total_favorites": 1,
          "total_watched": 34,
          "average_views": 11
      }
      ```

    - **Usuários**
        - Adiciona usuário: POST - /api/users

          **Tipo de Requisição:** POST

          **Exemplo de Corpo da Requisição:**
          ```json
          {
              "name": "Luiz Paulino",
              "email": "luizpaulino@email.com"
          }
          ```

          **Exemplo de Resposta:**
          ```json
          {
              "id": "7da7cc50-5690-445c-8571-c1314d8b52db",
              "name": "Luiz Paulino",
              "email": "luizpaulino@email.com"
          }
          ```

        - Busca usuário: GET - /api/users/{idUsuario}

          **Exemplo de Resposta:**
          ```json
          {
              "id": "7da7cc50-5690-445c-8571-c1314d8b52db",
              "name": "Luiz Paulino",
              "email": "luizpaulino@email.com"
          }
          ```
        - Atualiza usuário: PUT - /api/users/{idUsuario}

          **Tipo de Requisição:** POST

          **Exemplo de Corpo da Requisição:**
          ```json
          {
              "name": "Luiz Paulino",
              "email": "luizpaulino@email.com"
          }
          ```

          **Exemplo de Resposta:**
          ```json
          {
              "id": "7da7cc50-5690-445c-8571-c1314d8b52db",
              "name": "Luiz Paulino",
              "email": "luizpaulino@email.com"
          }
          ```
        - Ações do Usuário:
      
            - Adiciona vídeo favorito: POST - /api/users/{idUsuario}/favorite/{idVideo}

              **Resposta:** 200 OK

            - Busca recomendações: GET - /api/users/{idUsuario}/recommendations

              **Tipo de Requisição:** GET

              **Exemplo de Resposta:**
              ```json
              {
                  "content": [
                      {
                          "id": "4b65c3fe-ffb1-4672-9fca-3d61ec1dc62b",
                          "title": "Second movie",
                          "description": "Second movie to test",
                          "url": "https://postech-streaming-upload.s3.sa-east-1.amazonaws.com/4b65c3fe-ffb1-4672-9fca-3d61ec1dc62b.mp4",
                          "category": "Comedy",
                          "publication_date": "27/01/2024"
                      }
                  ],
                  "pageable": {
                      "pageNumber": 0,
                      "pageSize": 10,
                      "sort": {
                          "empty": true,
                          "sorted": false,
                          "unsorted": true
                      },
                      "offset": 0,
                      "unpaged": false,
                      "paged": true
                  },
                  "last": true,
                  "totalElements": 1,
                  "totalPages": 1,
                  "size": 10,
                  "number": 0,
                  "sort": {
                      "empty": true,
                      "sorted": false,
                      "unsorted": true
                  },
                  "first": true,
                  "numberOfElements": 1,
                  "empty": false
              }
              ```