package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.EmployeeUpdateDao;
import exception.E012Exception;
import exception.E022Exception;
import exception.E070Exception;
import exception.E090Exception;
import model.EmployeeBean;

/**
 * 利用者が入力した社員番号から更新入力画面に社員情報を表示する.
 * <br>利用者が入力した社員番号が登録されているか確認する.
 *
 * @version 1.0 JDK1.8.0_151
 * @author 中原大誠（電子開発学園）
 *
 * @see java.io.IOException
 * @see javax.servlet.RequestDispatcher
 * @see javax.servlet.ServletException
 * @see javax.servlet.annotation.WebServlet
 * @see javax.servlet.http.HttpServlet
 * @see javax.servlet.http.HttpServletRequest
 * @see javax.servlet.http.HttpServletResponse
 * @see javax.servlet.http.HttpSession
 * @see dao.EmployeeUpdateDao
 * @see exception.E012Exception
 * @see exception.E022Exception
 * @see exception.E070Exception
 * @see exception.E090Exception
 * @see model.EmployeeBean
 *
 */
@WebServlet("/EmployeeUpdateSearch")
public class EmployeeUpdateSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeUpdateSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 *  社員表の情報を更新する際に更新入力画面に社員情報を表示する.
	 *  <br>EmployeeUpdateDaoのsearchEmployeeメソッドを呼ぶ
	 *  <br>社員情報を表示するためにemployeeUpdate.jspにフォワードする.
	 *
	 * @exception E090Exception システムエラー
	 * @exception E070Exception セッションタイムアウトエラー
	 * @exception E012Exception 社員番号が整数でない
	 * @exception E022Exception 社員番号が登録されていない
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("UTF-8");
		Error er=new Error();
		HttpSession session=request.getSession();
		String id=(String) session.getAttribute("id");

 		 if(id==null) {
		 E070Exception e070=new E070Exception("セッションタイムアウト　ログインし直してください");
		 er.doPost(request, response, e070.getErrorCode(), e070.getMessage());
		 return;
      }
		int employeeNo=0;
		try {
		employeeNo=Integer.parseInt(request.getParameter("employeeNo"));
		}catch(NumberFormatException e) {
			E012Exception e012=new E012Exception("社員番号は整数を入力してください");

			er.doPost(request, response,e012.getErrorCode(),e012.getMessage());
		return;

		}
	    EmployeeUpdateDao ed=new EmployeeUpdateDao();
	    EmployeeBean em=new EmployeeBean();
	    try {
			em=ed.searchEmployee(employeeNo);
		} catch (E090Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	    int check=em.getCheck();

	    //社員番号が登録されていない場合
	    if(check==1) {
	    	E022Exception e022=new E022Exception("入力された社員番号が登録されていません");
	    	er.doPost(request, response, e022.getErrorCode(), e022.getMessage());
	    	return;
	    }

	    //更新情報入力画面へフォワード
       request.setAttribute("em", em);
	    RequestDispatcher dispatch=request.getRequestDispatcher("employeeUpdate.jsp");
		dispatch.forward(request, response);
	}

}
