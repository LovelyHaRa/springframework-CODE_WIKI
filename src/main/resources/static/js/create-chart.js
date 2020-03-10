$(function () {
    createMainChart('post');
    createMainChart('user');
    createMainChart('barcode');
    createMainChart('document')
});

function createMainChart(type) {
    const chartLabels = [];
    const chartData = [];
    $.post('/api/' + type + '/count/week', data=> {
        $.each(data, (index, item)=> {
            chartLabels.push(item['date']);
            chartData.push((item['count']));
        });
        createChart();
    }, 'json');

    console.log(chartLabels);
    console.log(chartData);

    let bgColor;
    let bdColor;

    switch (type) {
        case 'post':
            bgColor = 'rgba(254, 118, 122, 0.3)';
            bdColor = 'rgba(254, 118, 122, 1)';
            break;
        case 'user':
            bgColor = 'rgba(54, 162, 235, 0.3)';
            bdColor = 'rgba(54, 162, 235, 1)';
            break;
        case 'barcode':
            bgColor = 'rgba(255, 206, 86, 0.3)';
            bdColor = 'rgba(255, 206, 86, 1)';
            break;
        case 'document':
            bgColor = 'rgba(52, 58, 64, 0.3)';
            bdColor = 'rgba(52, 58, 64, 1)';
            break;
        default:
            bgColor = 'rgba(254, 118, 122, 0.3)';
            bdColor = 'rgba(254, 118, 122, 1)';
            break;
    }

    const chartLineData = {
        labels: chartLabels,
        datasets: [
            {
                label: 'created-date',
                data: chartData,
                backgroundColor: bgColor,
                borderColor: bdColor,
            }
        ],
    };

    const chartInOptions = {
        scales: {
            xAxes: [{
                gridLines: {
                    color: 'rgba(0, 0, 0, 0)'
                }
            }],
            yAxes: [{
                gridLines: {
                    color: 'rgba(0, 0, 0, 0)'
                },
                ticks: {
                    beginAtZero:true
                }
            }]
        },
        legend: {
            display: false
        }
    };

    function createChart() {
        let ctx;
        switch (type) {
            case 'post':
                ctx = $('#board-chart');
                break;
            case 'user':
                ctx = $('#user-chart');
                break;
            case 'barcode':
                ctx = $('#barcode-chart');
                break;
            case 'document':
                ctx = $('#document-chart');
                break;
            default:
                break;
        }
        const mainChart = new Chart.Line(ctx, {
            data: chartLineData,
            options: chartInOptions
        });
    }
}