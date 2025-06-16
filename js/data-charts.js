// 体重变化趋势
const weightCtx = document.getElementById('weight-chart').getContext('2d');
new Chart(weightCtx, {
    type: 'line',
    data: {
        labels: ['4月', '5月', '6月', '7月'],
        datasets: [{
            label: '体重 (kg)',
            data: [70, 68.5, 67.8, 66.5],
            borderColor: '#36a2eb',
            backgroundColor: 'rgba(54,162,235,0.1)',
            fill: true,
            tension: 0.4
        }]
    },
    options: {
        responsive: true,
        plugins: { legend: { display: false } }
    }
});

// 运动类型分布
const workoutTypesCtx = document.getElementById('workout-types-chart').getContext('2d');
new Chart(workoutTypesCtx, {
    type: 'doughnut',
    data: {
        labels: ['有氧', '力量', '瑜伽', '其他'],
        datasets: [{
            data: [40, 35, 15, 10],
            backgroundColor: ['#ff6384', '#36a2eb', '#ffcd56', '#4bc0c0']
        }]
    },
    options: {
        responsive: true,
        plugins: { legend: { position: 'bottom' } }
    }
});

// 每周锻炼时长
const weeklyDurationCtx = document.getElementById('weekly-duration-chart').getContext('2d');
new Chart(weeklyDurationCtx, {
    type: 'bar',
    data: {
        labels: ['第1周', '第2周', '第3周', '第4周'],
        datasets: [{
            label: '锻炼时长 (小时)',
            data: [5, 6, 5.5, 7],
            backgroundColor: '#4bc0c0'
        }]
    },
    options: {
        responsive: true,
        plugins: { legend: { display: false } }
    }
});

// 身体指标对比
const bodyMetricsCtx = document.getElementById('body-metrics-chart').getContext('2d');
new Chart(bodyMetricsCtx, {
    type: 'radar',
    data: {
        labels: ['体脂率', 'BMI', '肌肉量', '基础代谢'],
        datasets: [{
            label: '当前',
            data: [18, 22, 60, 1500],
            backgroundColor: 'rgba(255,99,132,0.2)',
            borderColor: '#ff6384'
        }, {
            label: '三月前',
            data: [20.1, 23.5, 56, 1450],
            backgroundColor: 'rgba(54,162,235,0.2)',
            borderColor: '#36a2eb'
        }]
    },
    options: {
        responsive: true
    }
});