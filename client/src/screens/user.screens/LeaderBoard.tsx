import React, { useEffect, useState } from "react";
import Card_LeaderBoard from "../../components/user.components/Card_LeaderBoard";
import { Table, Select } from "antd";
import type { TableProps } from "antd";
import "../../assets/css/Leader_board.css";
import {
  getAllYearsLeadBoard,
  getDataLeaderBoard,
} from "../../apiService/Score.service";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store/store";
interface DataType {
  key: string;
  rank: number;
  userInfor: {
    avatarUrl: string;
    username: string;
    fullname: string;
    email: string;
  };
  time: number;
  score: number;
  mine: boolean;
}
interface IDataResponse {
  avatarUrl: string;
  email: string;
  fullName: string;
  learningTime: number;
  mine: boolean;
  score: number;
  userId: number;
  username: string;
}

interface IYear {
  value: string;
  label: string;
}
const LeaderBoard: React.FC = () => {
  const [valueYear, setValueYear] = useState<string>("2024");
  const [dataCardLeaderBoard, setDataCardLeaderBoard] = useState<DataType[]>(
    []
  );
  const [dataLeaderBoardTable, setDataLeaderBoardTable] = useState<DataType[]>(
    []
  );

  const [dataYears, setDataYears] = useState<IYear[]>([]);
  const username = useSelector((state: RootState) => state.user.user.username);

  const columns: TableProps<DataType>["columns"] = [
    {
      title: "Rank",
      dataIndex: "rank",
      key: "rank",
      width: 100,
      render: (text) => <p className="font-medium">{text}</p>,
    },
    {
      title: "Username",
      dataIndex: "userInfor",
      key: "userInfor",
      render: (data: { avatarUrl: string; username: string }) => {
        return (
          <>
            <div className="flex items-center gap-2.5">
              <div className="relative w-8 h-8 rounded-full">
                <img
                  src={data.avatarUrl}
                  alt=""
                  className="absolute w-full h-full object-cover object-center rounded-full"
                />
              </div>
              <div className="font-normal">
                {data.username === username
                  ? `${data.username} (You)`
                  : `${data.username}`}
              </div>
            </div>
          </>
        );
      },
    },
    {
      title: "Time",
      dataIndex: "time",
      key: "time",
      render: (text) => <p className="text-[16px]">{text}</p>,
    },
    {
      title: "Score",
      dataIndex: "score",
      key: "score",
      render: (text) => <p className="text-[16px]">{text}</p>,
    },
  ];

  const handleChange = (value: string) => {
    setValueYear(value);
  };

  const fetchDataLeaderBoard = async (valueYear: string) => {
    const result = await getDataLeaderBoard(valueYear);
    if (result && result.leaderboardUserDTOs) {
      const dataTmp: DataType[] = result.leaderboardUserDTOs.map(
        (item: IDataResponse, index: number) => ({
          key: index.toString(),
          rank: index + 1,
          userInfor: {
            avatarUrl: item.avatarUrl,
            username: item.username,
            email: item.email,
            fullname: item.fullName,
          },
          time: item.learningTime,
          score: item.score,
          mine: item.mine,
        })
      );

      let reorderedData: DataType[] = [];
      if (dataTmp.length >= 3) {
        reorderedData = [dataTmp[1], dataTmp[0], dataTmp[2]];
      } else if (dataTmp.length === 2) {
        reorderedData = [dataTmp[1], dataTmp[0]];
      } else if (dataTmp.length === 1) {
        reorderedData = [dataTmp[0]];
      }

      setDataCardLeaderBoard(reorderedData);

      if (dataTmp.length > 3) {
        setDataLeaderBoardTable(dataTmp.slice(3));
      }
    }
  };

  const fetchAllYears = async () => {
    const result = await getAllYearsLeadBoard();
    if (result && result.data) {
      const dataTmp: IYear[] = result.data.map((item: string) => ({
        value: item,
        label: item,
      }));
      setDataYears(dataTmp);
    }
  };
  const rowClassName = (record: DataType) => {
    return record.mine ? "colorText" : "";
  };

  useEffect(() => {
    fetchDataLeaderBoard(valueYear);
  }, [valueYear]);

  useEffect(() => {
    fetchAllYears();
  }, []);

  return (
    <>
      <div className="bg-gray-100 pb-12">
        <div className="max-w-screen-2xl mx-auto px-5 pt-6">
          <div className="flex items-center justify-between px-2.5">
            <div>
              <span className="text-4xl font-semibold">Ranking</span>
            </div>
            <div className="flex items-center justify-center gap-6">
              <div className="text-gray-600 text-lg">Year</div>
              <div>
                <Select
                  defaultValue={valueYear}
                  style={{ width: 120 }}
                  onChange={handleChange}
                  options={dataYears}
                />
              </div>
            </div>
          </div>
          <div className="flex justify-center gap-8 mt-10 items-end">
            {dataCardLeaderBoard &&
              dataCardLeaderBoard.length > 0 &&
              dataCardLeaderBoard.map((item: DataType, index: number) => {
                if (index <= 2)
                  return (
                    <Card_LeaderBoard
                      username={item.userInfor.username}
                      email={item.userInfor.email}
                      ranking={item.rank}
                      avatarUrl={item.userInfor.avatarUrl}
                      score={item.score}
                      learningTime={item.time}
                    />
                  );
              })}
          </div>
          <div className="mt-12">
            <Table
              columns={columns}
              dataSource={dataLeaderBoardTable}
              pagination={false}
              rowClassName={rowClassName}
            />
          </div>
        </div>
      </div>
    </>
  );
};
export default LeaderBoard;
