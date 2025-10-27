from flask import Blueprint, render_template, redirect, url_for, flash, request
from flask_login import login_required, current_user
from app import db
from app.models import User, Post

bp = Blueprint('main', __name__)

@bp.route('/')
@login_required
def index():
    posts = current_user.get_feed_posts().all()
    return render_template('index.html', posts=posts)

@bp.route('/post/new', methods=['POST'])
@login_required
def new_post():
    content = request.form.get('content')
    
    if not content:
        flash('O post não pode estar vazio', 'error')
        return redirect(url_for('main.index'))
    
    post = Post(content=content, author=current_user)
    db.session.add(post)
    db.session.commit()
    
    flash('Post criado com sucesso!', 'success')
    return redirect(url_for('main.index'))

@bp.route('/post/delete/<int:post_id>', methods=['POST'])
@login_required
def delete_post(post_id):
    post = Post.query.get_or_404(post_id)
    
    if post.author != current_user:
        flash('Você não tem permissão para deletar este post', 'error')
        return redirect(url_for('main.index'))
    
    db.session.delete(post)
    db.session.commit()
    
    flash('Post deletado com sucesso!', 'success')
    return redirect(url_for('main.index'))

@bp.route('/profile/<username>')
@login_required
def profile(username):
    user = User.query.filter_by(username=username).first_or_404()
    posts = user.posts.order_by(Post.created_at.desc()).all()
    return render_template('profile.html', user=user, posts=posts)

@bp.route('/profile/edit', methods=['GET', 'POST'])
@login_required
def edit_profile():
    if request.method == 'POST':
        bio = request.form.get('bio', '')
        current_user.bio = bio
        db.session.commit()
        
        flash('Perfil atualizado com sucesso!', 'success')
        return redirect(url_for('main.profile', username=current_user.username))
    
    return render_template('edit_profile.html')

@bp.route('/follow/<username>', methods=['POST'])
@login_required
def follow(username):
    user = User.query.filter_by(username=username).first_or_404()
    
    if user == current_user:
        flash('Você não pode seguir a si mesmo', 'error')
        return redirect(url_for('main.profile', username=username))
    
    current_user.follow(user)
    db.session.commit()
    
    flash(f'Você agora está seguindo {username}!', 'success')
    return redirect(url_for('main.profile', username=username))

@bp.route('/unfollow/<username>', methods=['POST'])
@login_required
def unfollow(username):
    user = User.query.filter_by(username=username).first_or_404()
    
    if user == current_user:
        flash('Você não pode deixar de seguir a si mesmo', 'error')
        return redirect(url_for('main.profile', username=username))
    
    current_user.unfollow(user)
    db.session.commit()
    
    flash(f'Você deixou de seguir {username}', 'success')
    return redirect(url_for('main.profile', username=username))

@bp.route('/users')
@login_required
def users():
    all_users = User.query.filter(User.id != current_user.id).all()
    return render_template('users.html', users=all_users)
