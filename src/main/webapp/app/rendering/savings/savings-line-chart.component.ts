import { Component, OnDestroy, OnInit } from '@angular/core';
import { EChartOption } from 'echarts';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'jhi-rendering-savings',
  templateUrl: './savings-line-chart.component.html',
})
export class SavingsLineChartComponent implements OnInit, OnDestroy {
  chartOption: EChartOption | undefined;

  theme: any = {
    color: [
      '#2ec7c9',
      '#b6a2de',
      '#5ab1ef',
      '#ffb980',
      '#d87a80',
      '#8d98b3',
      '#e5cf0d',
      '#97b552',
      '#95706d',
      '#dc69aa',
      '#07a2a4',
      '#9a7fd1',
      '#588dd5',
      '#f5994e',
      '#c05050',
      '#59678c',
      '#c9ab00',
      '#7eb00a',
      '#6f5553',
      '#c14089',
    ],

    title: {
      textStyle: {
        fontWeight: 'normal',
        color: '#008acd',
      },
    },

    visualMap: {
      itemWidth: 15,
      color: ['#5ab1ef', '#e0ffff'],
    },

    toolbox: {
      iconStyle: {
        normal: {
          borderColor: '#2ec7c9',
        },
      },
    },

    tooltip: {
      backgroundColor: 'rgba(50,50,50,0.5)',
      axisPointer: {
        type: 'line',
        lineStyle: {
          color: '#008acd',
        },
        crossStyle: {
          color: '#008acd',
        },
        shadowStyle: {
          color: 'rgba(200,200,200,0.2)',
        },
      },
    },

    dataZoom: {
      dataBackgroundColor: '#efefff',
      fillerColor: 'rgba(182,162,222,0.2)',
      handleColor: '#008acd',
    },

    grid: {
      borderColor: '#eee',
    },

    categoryAxis: {
      axisLine: {
        lineStyle: {
          color: '#839496',
        },
      },
      splitLine: {
        lineStyle: {
          color: ['#eee'],
        },
      },
    },

    valueAxis: {
      axisLine: {
        lineStyle: {
          color: '#839496',
        },
      },
      splitArea: {
        show: true,
        areaStyle: {
          color: ['rgba(250,250,250,0.1)', 'rgba(200,200,200,0.1)'],
        },
      },
      splitLine: {
        lineStyle: {
          color: ['#eee'],
        },
      },
    },

    timeline: {
      lineStyle: {
        color: '#008acd',
      },
      controlStyle: {
        color: '#008acd',
        borderColor: '#008acd',
      },
      symbol: 'emptyCircle',
      symbolSize: 3,
    },

    line: {
      smooth: true,
      symbol: 'emptyCircle',
      symbolSize: 3,
    },

    candlestick: {
      itemStyle: {
        color: '#d87a80',
        color0: '#2ec7c9',
      },
      lineStyle: {
        width: 1,
        color: '#d87a80',
        color0: '#2ec7c9',
      },
      areaStyle: {
        color: '#2ec7c9',
        color0: '#b6a2de',
      },
    },

    scatter: {
      symbol: 'circle',
      symbolSize: 4,
    },

    map: {
      itemStyle: {
        color: '#ddd',
      },
      areaStyle: {
        color: '#fe994e',
      },
      label: {
        color: '#d87a80',
      },
    },

    graph: {
      itemStyle: {
        color: '#d87a80',
      },
      linkStyle: {
        color: '#2ec7c9',
      },
    },

    gauge: {
      axisLine: {
        lineStyle: {
          color: [
            [0.2, '#2ec7c9'],
            [0.8, '#5ab1ef'],
            [1, '#d87a80'],
          ],
          width: 10,
        },
      },
      axisTick: {
        splitNumber: 10,
        length: 15,
        lineStyle: {
          color: 'auto',
        },
      },
      splitLine: {
        length: 22,
        lineStyle: {
          color: 'auto',
        },
      },
      pointer: {
        width: 5,
      },
    },
  };

  constructor(protected translateService: TranslateService) {}

  ngOnDestroy(): void {}

  ngOnInit(): void {
    let months = [
      'accountManagerApp.savings.xaxis.january',
      'accountManagerApp.savings.xaxis.february',
      'accountManagerApp.savings.xaxis.march',
      'accountManagerApp.savings.xaxis.april',
      'accountManagerApp.savings.xaxis.may',
      'accountManagerApp.savings.xaxis.june',
      'accountManagerApp.savings.xaxis.july',
      'accountManagerApp.savings.xaxis.august',
      'accountManagerApp.savings.xaxis.september',
      'accountManagerApp.savings.xaxis.october',
      'accountManagerApp.savings.xaxis.november',
      'accountManagerApp.savings.xaxis.december',
    ];
    this.translateService.get(months).subscribe(value => (months = Object.values(value)));

    this.chartOption = {
      xAxis: { type: 'category', boundaryGap: false, data: months },
      yAxis: { type: 'value' },
      series: [
        {
          data: [820, 932, 901, 934, 1290, 1430, 1550, 1200, 1650.145, 1680.189],
          type: 'line',
          areaStyle: {},
        },
      ],
    };
  }
}
