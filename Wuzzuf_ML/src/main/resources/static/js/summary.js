let table = document.getElementById("mySchemaTable");
iterator(fullSchemaTable, table, 25);
table = document.getElementById("mySummaryTable");
iterator(schemaTable, table, schemaTable.length);

function iterator(data, table, iterations){
    for (let i = 0; i < iterations; i++){
        const obj = JSON.parse(data[i]);
        buildTable(obj, table)
    }
}

function buildTable(data, table) {
    const row = `<tr>
                <th scope="row">${data.summary}</th>
                <td>${data.Title}</td>
                <td>${data.Company}</td>
                <td>${data.Location}</td>
                <td>${data.Type}</td>
                <td>${data.Level}</td>
                <td>${data.YearsExp}</td>
                <td>${data.Country}</td>
                <td>${data.Skills}</td>
            </tr>`;
    table.innerHTML += row;
}