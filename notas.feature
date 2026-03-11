Feature: API REST de Notas
  Pruebas de aceptación para los endpoints de la API de Notas

  Background:
    * url 'http://localhost:' + karate.properties['server.port']
    * header Content-Type = 'application/json'

  # ─────────────────────────────────────────────────────
  # POST /notas — Crear una nota
  # ─────────────────────────────────────────────────────
  Scenario: Crear una nota exitosamente
    Given path '/notas'
    And request { titulo: 'Mi primera nota', contenido: 'Este es el contenido de prueba' }
    When method POST
    Then status 201
    And match response.id != null
    And match response.titulo == 'Mi primera nota'
    And match response.contenido == 'Este es el contenido de prueba'
    And match response.fechaCreacion != null

  Scenario: Crear múltiples notas con datos distintos
    Given path '/notas'
    And request { titulo: 'Nota de compras', contenido: 'Leche, pan, huevos' }
    When method POST
    Then status 201
    And match response.titulo == 'Nota de compras'

    Given path '/notas'
    And request { titulo: 'Nota de trabajo', contenido: 'Reunión a las 10am' }
    When method POST
    Then status 201
    And match response.titulo == 'Nota de trabajo'

  # ─────────────────────────────────────────────────────
  # GET /notas — Obtener todas las notas
  # ─────────────────────────────────────────────────────
  Scenario: Obtener todas las notas retorna una lista
    # Primero crear una nota para garantizar que hay datos
    Given path '/notas'
    And request { titulo: 'Nota para listar', contenido: 'Contenido de listado' }
    When method POST
    Then status 201

    # Luego obtener todas las notas
    Given path '/notas'
    When method GET
    Then status 200
    And match response == '#array'
    And match each response contains { id: '#number', titulo: '#string', contenido: '#string' }

  Scenario: La lista de notas no está vacía después de crear una nota
    Given path '/notas'
    And request { titulo: 'Nota verificación lista', contenido: 'Verificando la lista' }
    When method POST
    Then status 201

    Given path '/notas'
    When method GET
    Then status 200
    And assert response.length >= 1

  # ─────────────────────────────────────────────────────
  # GET /notas/{id} — Obtener nota por ID
  # ─────────────────────────────────────────────────────
  Scenario: Obtener una nota específica por su id
    # Crear la nota y guardar el id
    Given path '/notas'
    And request { titulo: 'Nota para buscar', contenido: 'Buscando por id' }
    When method POST
    Then status 201
    * def notaId = response.id

    # Obtener por id
    Given path '/notas/' + notaId
    When method GET
    Then status 200
    And match response.id == notaId
    And match response.titulo == 'Nota para buscar'
    And match response.contenido == 'Buscando por id'

  Scenario: Obtener una nota con id inexistente retorna 404
    Given path '/notas/999999'
    When method GET
    Then status 404

  # ─────────────────────────────────────────────────────
  # DELETE /notas/{id} — Eliminar una nota
  # ─────────────────────────────────────────────────────
  Scenario: Eliminar una nota existente
    # Crear nota
    Given path '/notas'
    And request { titulo: 'Nota a eliminar', contenido: 'Esta nota será eliminada' }
    When method POST
    Then status 201
    * def notaId = response.id

    # Eliminar la nota
    Given path '/notas/' + notaId
    When method DELETE
    Then status 204

    # Verificar que ya no existe
    Given path '/notas/' + notaId
    When method GET
    Then status 404

  Scenario: Eliminar una nota inexistente retorna 404
    Given path '/notas/999999'
    When method DELETE
    Then status 404

  # ─────────────────────────────────────────────────────
  # Flujo completo: CRUD
  # ─────────────────────────────────────────────────────
  Scenario: Flujo completo - crear, obtener, verificar en lista y eliminar
    # 1. Crear
    Given path '/notas'
    And request { titulo: 'Flujo completo', contenido: 'Nota del flujo completo' }
    When method POST
    Then status 201
    * def idNota = response.id

    # 2. Obtener por id
    Given path '/notas/' + idNota
    When method GET
    Then status 200
    And match response.titulo == 'Flujo completo'

    # 3. Verificar en lista
    Given path '/notas'
    When method GET
    Then status 200
    * def encontrada = karate.filter(response, function(n){ return n.id == idNota })
    And assert encontrada.length == 1

    # 4. Eliminar
    Given path '/notas/' + idNota
    When method DELETE
    Then status 204

    # 5. Confirmar eliminación
    Given path '/notas/' + idNota
    When method GET
    Then status 404
