/*
Vishal Kulkarni
*/
#include<iostream>
#include<vector> 
#include <iterator>
#include <algorithm>
using namespace std;

int number=0;
template <class T>

class Stack 
{ 
   private: 
      vector< vector<int> > move;   
   public: 
      void push(vector<int>); 
      void pop();                        
      int size() const;
      int print();
      void all();
      bool empty() const
	  {      
         return move.empty(); 
      } 
}; 

template <class T>
void Stack<T>::push (vector <int> elem) 
{  
   move.push_back(elem);    
} 

template <class T>
void Stack<T>::pop () 
{ 
   move.pop_back();         
} 

template <class T>
int Stack<T>:: print() 
{
	int z;
	int x;
	std::vector< int > holes;
    for(int i=0 ; i<move.size(); i++)
	{
	    cout<<"-----------------------"<<endl;
        cout<<"[from,over,to]= "<< move[i][0] << " " << move[i][1] << " " << move[i][2] <<endl;
        cout<<"-----------------------"<<endl;
		z = 13-i;
		
		holes.push_back(move[i][0]);
		holes.push_back(move[i][1]);
		
		if(std::find(holes.begin(), holes.end(), move[i][2]) != holes.end())
		{
		    //int *it = std::find((holes.begin()), (holes.end()), move[i][2]);
		   
		   //holes.erase(it);
		   holes.erase(std::remove(holes.begin(), holes.end(), move[i][2]), holes.end());
		}
		x =x +2;
		//holes.push_back(move[i][2]);
		
		//std::find(std::begin(holes), std::end(holes), counter) != std::end(holes);
		// When the element is not found, std::find returns the end of the range
		//(hole != std::end(holes))
		
        int counter = 0;
            for (int l = 0; l < 5; l++) {
            cout<<"  ";
                for (int k = 0; k < 4 - l; k++) {
                    cout<<"  ";
                    }
			
                for (int j = 0; j < 5; j++) {
                    if (j <= l) {
                        if (std::find((holes.begin()), (holes.end()), counter) != holes.end() ) {
                            //cout<<"here"<<move[i][0];
                            cout<<"o   ";
                        } 
                        else {
                            cout<<"x   ";
                        }counter ++;
                        //cout<<counter;
                        
                }

            }
            cout<<""<<endl;

        }
	}
    

    return 0;
}

template <class T>
void Stack<T>:: all() 
{ 
    for(int j=0; j<13; j++) 
	{
        move.pop_back();
    }    
}

vector< vector<int> > checkIfEmpty(int board[][5]) 
{
    vector< vector<int> > vec;
    for(int i=0; i<5; i++) 
	{
	for(int j=0; j<i+1; j++) 
	{
            if(board[i][j] == 0) 
			{
		vector<int> pos;
		pos.push_back(i);
		pos.push_back(j);
		vec.push_back(pos);
            }
	    }
    }
    return vec;	
}
int convert(int a, int b) 
{
    int c = -1 ;
    int flag = 0; 
    int num=0;
    for(int i=0; i<5; i++) 
	{
	    for(int j=0; j<i+1; j++) 
		{
	        c++;
	        if(i==a && j==b) 
			{ 
                num = c;
                flag= 1;
	    	    break;
	        }	
	    }
	    if(flag == 1) break;
    }		
    return num;
}

int if_empty(int a[][5],int x,int y) 
{
    return a[x][y];
}

vector< vector<int> > findMoves(int board[][5]) 
{
    int count=0;
    vector< vector<int> > vec;
    vector< vector<int> > emp;
    emp=checkIfEmpty(board);
    for(int z=0; z<emp.size(); z++) 
	{
        vector<int> pos;
        pos=emp[z];
        int t;
        int var1;
        int var2;
        for(int i=-2; i<=2; i+=2) 
		{
	        for(int j=-2; j<=2; j+=2) 
			{
                var1=0;
	 	        var2=0;
	 	        if(i == 2 && j == -2) continue;
	 	        if(i == -2 && j == 2) continue;
	 	        if(i == 0 && j == 0) continue;
                var1=pos[0]+i;
	 	        var2=pos[1]+j;
	 	        if(var1>=0 && var2>=0 && var1<5 && var2<5 && var1>=var2 && if_empty(board,var1,var2)==1) 
				{
		            var1=(var1+pos[0])/2;
                    var2=(var2+pos[1])/2;
                    if(if_empty(board,var1,var2)==1) 
					{
                        vector<int> move;
                        move.push_back( convert(pos[0]+i,pos[1]+j) );
			            move.push_back( convert(var1,var2) );
			            move.push_back( convert(pos[0],pos[1]) );
			            vec.push_back(move);
                    }
	 	        }
	        }
        }
    }
    return vec;
}
void changeBoard(int play_board[][5], int no, int reset) 
{
    int c = 0;
    int flag = 0; 
    for(int i=0; i<5; i++) 
	{
        for(int j=0; j<i+1; j++) 
		{
	    if(c == no)
			{
                play_board[i][j]=reset;
				flag=1;
				break;
			}
	    c++;
		}
	if(flag==1) break;		
    }
}        
void initialize (int board[][5], int no)
{
    int count=-1;
    for(int i=0; i<5; i++)
    {
        for(int j=0; j<i+1; j++)
        {
            count=count+1;
            if(no == count)
            {
                board[i][j]=0;
            }
            else
			{
                board[i][j]=1;
            }
        }
    }
}
int Hiq(int board[][5], int counting, Stack< vector<int> >& mystack) 
{
    if(counting == number)
	{
        if(counting == 13) 
		{
        	
            mystack.print();
            return 1;
        }
        cout << "The solution is" << endl;
        mystack.print();
        cout<< "Do you want a better solution?(yes/no)";
        int soln;
        cin >> soln;
        if(soln==1)
		{
            number++;
        }
        else 
		{
            return 1;
        }
    }
 	vector< vector<int> > a;
    a=findMoves(board);
 	for(int i=0; i<a.size(); i++) 
	{
 	    vector<int> t;
            t=a[i];
 	    counting++;
 	    changeBoard(board,t[0],0);
	    changeBoard(board,t[1],0);
	    changeBoard(board,t[2],1);
	    mystack.push(t);
	    if(Hiq(board, counting, mystack)==1)
		{
            return 1;
        }
	    else 
		{ 
		counting--;
		mystack.pop();
        changeBoard(board,t[0],1);
		changeBoard(board,t[1],1);
		changeBoard(board,t[2],0);
	    }
	}
    return 0;
}
int main()
{
    Stack< vector<int> > vec;
    int empty_hole;
    int pegs_allowed;
    cout << "Enter the position of empty hole: ";
    while(true) 
	{
        cin >> empty_hole;
        pegs_allowed = 1;
        number=14-pegs_allowed;
        if(empty_hole<15 && empty_hole>=0 && pegs_allowed>0 && pegs_allowed<14) 
		{
            int start_board[5][5];
            initialize(start_board, empty_hole);
            Hiq(start_board, 0, vec);
            break;
        }
        else cout << "incorrect Input. Please input again between 0 to 14";    
    }
    return 0;
}
