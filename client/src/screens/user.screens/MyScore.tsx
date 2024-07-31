import React, { useEffect, useState } from "react";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../../components/ui/select";
import { Bar, Line } from "react-chartjs-2";
import {
  Chart,
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import {
  getDataMyScore,
  getDataScoreByYear,
} from "../../apiService/Score.service";

// Đăng ký các thành phần cần thiết
Chart.register(
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend
);

type DataChartType = {
  scores: number[];
  days: number[];
};

type DataPersonal = {
  score: number;
  rank: number;
};

const MyScore: React.FC = () => {
  const [valueYear, setValueYear] = useState<string>("2024");
  const [valuePersonal, setValuePersonal] = useState<
    DataPersonal | undefined
  >();
  const [optionYear, setOptionYear] = useState<string[]>([]);
  const [dataChart, setDataChart] = useState<DataChartType | undefined>();

  const fetchDataYearOption = async () => {
    const result = await getDataMyScore();
    if (result?.label) {
      setOptionYear(result.label);
    }
    if (result?.data) {
      setDataChart(result.data);
    }
  };

  const fetchDataPersonal = async (year: string) => {
    const result = await getDataScoreByYear(year);
    if (result) {
      setValuePersonal({
        score: result.score || 0,
        rank: result.rank || 0,
      });
    }
  };

  useEffect(() => {
    fetchDataYearOption();
  }, []);

  useEffect(() => {
    fetchDataPersonal(valueYear);
  }, [valueYear]);

  const data = {
    labels: optionYear,
    datasets: [
      {
        label: "Score",
        data: dataChart?.scores,
        backgroundColor: "#861fa2",
        borderColor: "rgba(153, 102, 255, 1)",
        borderWidth: 1,
        yAxisID: "y1",
      },
      {
        label: "Days",
        data: dataChart?.days,
        backgroundColor: "rgba(75, 192, 192, 0.4)",
        borderColor: "rgba(75, 192, 192, 1)",
        borderWidth: 1,
        yAxisID: "y2",
      },
    ],
  };

  const options = {
    maintainAspectRatio: false,
    scales: {
      y1: {
        type: "linear" as const,
        position: "left" as const,
        beginAtZero: true,
        title: {
          display: true,
          text: "Score",
        },
      },
      y2: {
        type: "linear" as const,
        position: "right" as const,
        beginAtZero: true,
        grid: {
          drawOnChartArea: false,
        },
        title: {
          display: true,
          text: "Days",
        },
      },
    },
  };

  return (
    <div className="bg-gray-100 h-screen pt-4 mt-1">
      <div className="max-w-screen-2xl mx-auto h-7/8 rounded-[15px] border border-gray-300 flex px-8 py-2.5 items-center bg-white">
        <div className="w-[25%] border border-gray-300 rounded-2xl flex justify-between items-start p-5 h-auto">
          <div className="flex flex-col gap-5 w-[70%]">
            <div>
              <div className="text-lg font-medium">Score</div>
              <div
                className="text-3xl font-medium text-purple-700"
                style={{
                  display: "-webkit-box",
                  WebkitLineClamp: 1,
                  WebkitBoxOrient: "vertical",
                  overflow: "hidden",
                  textOverflow: "ellipsis",
                  whiteSpace: "normal",
                  wordBreak: "break-word",
                  color: "#861fa2",
                }}
                title={valuePersonal?.score.toString()}
              >
                {valuePersonal?.score}
              </div>
            </div>
            <div>
              <div className="text-lg font-medium">Rank</div>
              <div
                className="text-3xl font-medium text-purple-700"
                style={{ color: "#861fa2" }}
              >
                {valuePersonal?.rank}
              </div>
            </div>
          </div>
          <div>
            <Select onValueChange={(value) => setValueYear(value)}>
              <SelectTrigger className="w-28 h-8.5">
                <SelectValue placeholder="2024" />
              </SelectTrigger>
              <SelectContent>
                {optionYear.map((item) => (
                  <SelectItem value={item} key={item}>
                    {item}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
        </div>
        <div className="flex-1 p-5 w-[75%] h-full">
          <div className="w-full h-1/2">
            <Line data={data} options={options} />
          </div>
          <hr className="h-px w-full mt-5" />
          <div className="w-full h-1/2">
            <Bar data={data} options={options} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyScore;
