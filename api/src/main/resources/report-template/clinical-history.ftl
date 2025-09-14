<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <title>Historia clínica</title>
  <style>
    body { font-family: DejaVu Sans, Arial, sans-serif; font-size: 11pt; color: #222; }
    h1 { text-align:center; font-size: 16pt; margin: 0 0 10px 0; }
    .meta { font-size: 9pt; color: #666; text-align: center; margin-bottom: 18px; }
    .section-title { font-size: 12pt; font-weight: bold; margin: 14px 0 6px 0; }
    table { width: 100%; border-collapse: collapse; page-break-inside:auto; }
    th, td { border: 1px solid #ddd; padding: 6px; }
    th { background: #f0f0f0; text-align: left; }
    .muted { color: #666; font-size: 9pt; }
  </style>
</head>
<body>
  <h1>Historia clínica</h1>
  <div class="meta">
    Generado: ${generatedAt?html}
  </div>

  <div class="section-title">Paciente</div>
  <table>
    <tr><th>Identificador</th><td>${dto.patient.identifier!""}</td></tr>
    <tr><th>Nombre</th><td>${dto.patient.givenName!""} ${dto.patient.familyName!""}</td></tr>
    <tr><th>Género</th><td>${dto.patient.gender!""}</td></tr>
    <tr><th>Fecha de nacimiento</th><td>${dto.patient.birthdate!""}</td></tr>
    <tr><th>UUID</th><td class="muted">${dto.patient.uuid!""}</td></tr>
  </table>

  <#if dto.visits?size == 0>
    <p class="muted">No hay visitas registradas.</p>
  <#else>
    <#list dto.visits as v>
      <div class="section-title">Visita: ${v.uuid!""}</div>
      <table>
        <tr>
          <th>Tipo</th><td>${v.visitType!""}</td>
          <th>Inicio</th><td>${v.startDatetime!""}</td>
          <th>Fin</th><td>${v.stopDatetime!""}</td>
          <th>Ubicación</th><td>${v.location!""}</td>
        </tr>
      </table>

      <#if v.encounters?size == 0>
        <p class="muted">Sin encuentros.</p>
      <#else>
        <table>
          <thead>
            <tr>
              <th>Encuentro</th>
              <th>Tipo</th>
              <th>Fecha/Hora</th>
              <th>Ubicación</th>
              <th>Proveedor</th>
              <th>Concepto</th>
              <th>Valor</th>
              <th>Unidades</th>
            </tr>
          </thead>
          <tbody>
          <#list v.encounters as e>
            <#if e.observations?size == 0>
              <tr>
                <td>${e.uuid!""}</td>
                <td>${e.encounterType!""}</td>
                <td>${e.encounterDatetime!""}</td>
                <td>${e.location!""}</td>
                <td>${e.provider!""}</td>
                <td>-</td>
                <td>-</td>
                <td>-</td>
              </tr>
            <#else>
              <#list e.observations as o>
                <tr>
                  <td>${e.uuid!""}</td>
                  <td>${e.encounterType!""}</td>
                  <td>${e.encounterDatetime!""}</td>
                  <td>${e.location!""}</td>
                  <td>${e.provider!""}</td>
                  <td>${o.concept!""}</td>
                  <td>${o.value!""}</td>
                  <td>${o.units!""}</td>
                </tr>
              </#list>
            </#if>
          </#list>
          </tbody>
        </table>
      </#if>
    </#list>
  </#if>
</body>
</html>
