const table = document.getElementById("mySchemaTable");
iterator(data1, data2, table, 80);

function iterator(data1, data2, table, iterations){
    for (let i = 0; i < iterations; i++){
        const obj1 = JSON.parse(data1[i]);
        const obj2 = JSON.parse(data2[i]);
        console.log(obj2);
        buildTable(obj1, obj2, table)
    }
}

function buildTable(data1, data2, table) {
    const row = `<tr>
                <th scope="row">${data2.YearsExp}</td>
                <td>${data1}</th>
            </tr>`;
    table.innerHTML += row;
}